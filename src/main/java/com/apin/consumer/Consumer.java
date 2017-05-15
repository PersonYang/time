package com.apin.consumer;

import com.apin.timer.TimerTaskManagement;
import com.apin.utils.ApinDateUtil;
import com.apin.utils.ApinUtil;
import com.apin.utils.Util;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * Created by Administrator on 2016/11/11.
 */
@Service
public class Consumer {

    private static final Logger logger= LoggerFactory.getLogger(Consumer.class);

    private KafkaConsumer<String, String> consumer;

    @Autowired
    private TimerTaskManagement timerTaskManagement;

    public void init(){
        Properties props = new Properties();
        props.put("bootstrap.servers", ApinUtil.getProperty("bootstrap.servers"));
        props.put("group.id", ApinUtil.getProperty("group.id"));
        props.put("enable.auto.commit", ApinUtil.getProperty("enable.auto.commit"));
        props.put("auto.commit.interval.ms", ApinUtil.getProperty("auto.commit.interval.ms"));
        props.put("session.timeout.ms", ApinUtil.getProperty("session.timeout.ms"));
        props.put("key.deserializer", ApinUtil.getProperty("key.deserializer"));
        props.put("value.deserializer", ApinUtil.getProperty("value.deserializer"));

        consumer = new KafkaConsumer<>(props);
        String topics= ApinUtil.getProperty("kafka.consumer.topic");
        String [] topicArray=topics.split(",");
        List<String> topicList=Arrays.asList(topicArray);
        consumer.subscribe(topicList);

        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String startDate = simpleDateFormat.format(new Date());
        System.out.println(startDate+" timer-consumer is started");
    }

    public  void receiveMessage(){
        while (true) {
            long pollTimeout= ApinUtil.getLong("kafka.consumer.pool.timeout");
            ConsumerRecords<String, String> records = consumer.poll(pollTimeout);

            int interval= ApinUtil.getInt("kafka.consumer.poll.interval");
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (ConsumerRecord<String, String> record : records) {
                String msg=record.value();
                logger.debug("recv msg:{}",msg);
                try {
                    JSONObject param = new JSONObject(msg);
                    if(Util.isBlank(param,"dataJson","endDate","taskClassName","jobSuffix","jobGroupSuffix")){
                        throw new Throwable("param is blank");
                    }
                    JSONObject dataJson=param.getJSONObject("dataJson");
                    String endDateString=param.getString("endDate");
                    Date endDate= ApinDateUtil.parse(endDateString);
                    String taskClassName=param.getString("taskClassName");
                    Class taskClass=Class.forName(taskClassName);
                    String jobSuffix=param.getString("jobSuffix");
                    String jobGroupSuffix=param.getString("jobGroupSuffix");
                    timerTaskManagement.addTimerTask(taskClass,jobGroupSuffix,jobSuffix,endDate,Util.toHashMap(dataJson));

                }catch (Throwable e){
                    logger.error("error msg:{}",msg);
                    logger.error("exception caught with info:{}",Util.getTrace(e));
                }
            }
        }
    }

}
