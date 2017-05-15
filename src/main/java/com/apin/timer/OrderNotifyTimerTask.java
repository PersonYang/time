package com.apin.timer;

import com.apin.KafkaProducer;
import com.apin.dao.AccountMapper;
import com.apin.dao.ClientMapper;
import com.apin.dao.OrderMapper;
import com.apin.jedis.RedisType;
import com.apin.po.ApinAccount;
import com.apin.po.ApinClient;
import com.apin.po.Order;
import com.apin.utils.ApinUtil;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Created by Administrator on 2016/9/20.
 */
public class OrderNotifyTimerTask extends TimerTask{

    private static final Logger logger= LoggerFactory.getLogger(OrderNotifyTimerTask.class);

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ClientMapper clientMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap map=context.getJobDetail().getJobDataMap();
        long orderNo=map.getLong("orderNo");

        Order order=orderMapper.selectOrderByOrderNo(orderNo);
        int orderStatus=order.getStatus();
        if(orderStatus==1){
            try {
                String phone="";
                if(StringUtils.isBlank(order.getTravelAgencyId())){
                    ApinClient apinClient=clientMapper.selectClientWithId(order.getUserId());
                    phone=apinClient.getPhone();
                }else{
                    ApinAccount apinAccount=accountMapper.selectAccountById(order.getTravelAgencyId());
                    phone=apinAccount.getPhone();
                }
                JSONObject param=new JSONObject();
                param.put("orderNo",String.valueOf(order.getOrderNo()));
                param.put("customServicePhone",ApinUtil.getProperty("custom.service.phone"));
                JSONObject json=new JSONObject();
                json.put("templateCode","SMS_35960228");
                json.put("param",param.toString());
                json.put("user",phone);
                kafkaProducer.send("sms",json.toString());
            }catch(Throwable e){
                logger.error("fail to notice the user with the orderNo={},errorInfo:{}",orderNo,e.getCause());
                throw new JobExecutionException("fail to notice the user with the orderNo="+orderNo);
            }
        }
    }

    public KafkaProducer getKafkaProducer() {
        return kafkaProducer;
    }

    public void setKafkaProducer(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }
}
