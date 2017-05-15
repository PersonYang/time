package com.apin.timer;

import com.apin.dao.MerchantFlightInfoMapper;
import com.apin.dao.OrderMapper;
import com.apin.po.Order;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/22.
 */
public class RestPayTimerTask extends TimerTask{

    private static final Logger logger= LoggerFactory.getLogger(OrderTimerTask.class);

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private MerchantFlightInfoMapper merchantFlightInfoMapper;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap map=context.getJobDetail().getJobDataMap();
        long orderNo=map.getLong("orderNo");

        Order order=orderMapper.selectOrderByOrderNo(orderNo);
        int orderStatus=order.getStatus();
        if(orderStatus==6){
            expireOrder(order);
        }
    }

    private void expireOrder(Order order) throws JobExecutionException{
        //1.return the ticket to the merchant
//        Map<String,Object> map=new HashMap<>();
//        map.put("passengerNum",order.getPassengerNum());
//        map.put("updateTime",new Date());
//        map.put("journeyId",order.getJourneyId());
//
//        if(merchantFlightInfoMapper.increaseMerchantFlightInfoRemainTickets(map)<1){
//            throw new JobExecutionException("fail to update the remainTickertNum");
//        }

        //2.update the order to 0(expired)
        Order orderUpdate=new Order();
        orderUpdate.setOrderNo(order.getOrderNo());
        orderUpdate.setStatus(0);
        orderUpdate.setUpdateTime(new Date());
        if(orderMapper.updateOrderSelective(orderUpdate)<1){
            logger.error("fail to update the status of order:{} to 0", order.getOrderNo());
            throw new JobExecutionException("fail to update the status of order "+order.getOrderNo()+" to 0");
        }

    }
}
