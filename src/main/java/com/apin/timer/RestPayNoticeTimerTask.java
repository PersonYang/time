package com.apin.timer;

import com.apin.KafkaProducer;
import com.apin.dao.AccountMapper;
import com.apin.dao.ClientMapper;
import com.apin.dao.MerchantFlightDetailInfoMapper;
import com.apin.dao.OrderMapper;
import com.apin.jedis.Redis;
import com.apin.jedis.RedisType;
import com.apin.po.ApinAccount;
import com.apin.po.ApinClient;
import com.apin.po.MerchantFlightDetailInfo;
import com.apin.po.Order;
import com.apin.utils.ApinDateUtil;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/17.
 */
public class RestPayNoticeTimerTask extends TimerTask{

    private static final Logger logger= LoggerFactory.getLogger(RestPayNoticeTimerTask.class);

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private MerchantFlightDetailInfoMapper merchantFlightDetailInfoMapper;

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
        if(orderStatus==6){
            try {
                noticePayRest(order);
            }catch (Throwable e){
                throw new JobExecutionException("fail to notice the user tp pay for the rest with the error:"+e.getMessage());
            }
        }
    }

    private void noticePayRest(Order order) throws Throwable{
        long journeyId=order.getJourneyId();
        List<MerchantFlightDetailInfo> merchantFlightDetailInfoList=merchantFlightDetailInfoMapper.selectMatchedMerchantFlightDetailInfoByJourneyId(journeyId);

        StringBuffer journeyInfoSb=new StringBuffer();
        for(int i=0;i<merchantFlightDetailInfoList.size();i++){
            MerchantFlightDetailInfo merchantFlightDetailInfo=merchantFlightDetailInfoList.get(i);
            journeyInfoSb.append(merchantFlightDetailInfo.getDepartDate());//可以替换成merchantFlightDetailInfo.getDepartDate()
            journeyInfoSb.append(" ");
            journeyInfoSb.append(merchantFlightDetailInfo.getDepartTime());//可以替换成merchantFlightDetailInfo.getDepartTime()
            journeyInfoSb.append("从");
            journeyInfoSb.append(merchantFlightDetailInfo.getDepartPlace());//可以替换成merchantFlightDetailInfo.getDepartPlace()
            journeyInfoSb.append(merchantFlightDetailInfo.getDepartAirport());//可以替换成merchantFlightDetailInfo.getDepartAirport()
            journeyInfoSb.append("出发");
            journeyInfoSb.append("，");//中文逗号
            journeyInfoSb.append(merchantFlightDetailInfo.getArriveDate());//可以替换成merchantFlightDetailInfo.getArriveDate()
            journeyInfoSb.append(" ");
            journeyInfoSb.append(merchantFlightDetailInfo.getArriveTime());//可以替换成merchantFlightDetailInfo.getArriveTime()
            journeyInfoSb.append("抵达");
            journeyInfoSb.append(merchantFlightDetailInfo.getDestPlace());//可以替换成merchantFlightDetailInfo.getDestPlace()
            journeyInfoSb.append(merchantFlightDetailInfo.getArriveAirport());//可以替换成merchantFlightDetailInfo.getArriveAirport()
            if(i<merchantFlightDetailInfoList.size()-1) {
                journeyInfoSb.append("，");//中文逗号
            }
        }
        String journey=journeyInfoSb.toString();

        String phone="";
        if(StringUtils.isBlank(order.getTravelAgencyId())){
            ApinClient apinClient=clientMapper.selectClientWithId(order.getUserId());
            phone=apinClient.getPhone();
        }else{
            ApinAccount apinAccount=accountMapper.selectAccountById(order.getTravelAgencyId());
            phone=apinAccount.getPhone();
        }

        JSONObject param =new JSONObject();
        param.put("createTime",ApinDateUtil.format(order.getCreateTime(), 0));
        param.put("journey",journey);
        JSONObject json=new JSONObject();
        json.put("templateCode","SMS_35995296");
        json.put("param",param.toString());
        json.put("user",phone);
        kafkaProducer.send("sms", json.toString());
    }

    public KafkaProducer getKafkaProducer() {
        return kafkaProducer;
    }

    public void setKafkaProducer(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }
}
