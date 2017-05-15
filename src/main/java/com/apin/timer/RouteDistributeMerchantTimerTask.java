package com.apin.timer;

import com.apin.dao.MerchantRoutePriceInfoMapper;
import com.apin.dao.RouteDistributeMerchantInfoMapper;
import com.apin.dao.RouteMapper;
import com.apin.po.MerchantRoutePriceInfo;
import com.apin.po.Route;
import com.apin.po.RouteDistributeMerchantInfo;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.concurrent.ExecutorService;

/**
 * Created by Administrator on 2016/9/23.
 */
public class RouteDistributeMerchantTimerTask extends TimerTask{

    private static final Logger logger= LoggerFactory.getLogger(RouteDistributeMerchantTimerTask.class);

    @Autowired
    private RouteDistributeMerchantInfoMapper routeDistributeMerchantInfoMapper;

    @Autowired
    private RouteMapper routeMapper;

    @Autowired
    private MerchantRoutePriceInfoMapper merchantRoutePriceInfoMapper;

    @Autowired
    private TimerTaskManagement timerTaskManagement;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap map=context.getJobDetail().getJobDataMap();
        Long routeNo=map.getLong("routeNo");
        Route route=routeMapper.getRouteInfoByRouteNo(routeNo);
        if(route.getRouteStatus()==1){
            Route routeUpdate=new Route();
            routeUpdate.setRouteNo(routeNo);
            routeUpdate.setUpdateTime(new Date());
            routeUpdate.setRouteStatus(4);
            if(routeMapper.updateRouteSelective(routeUpdate)<1){
                throw new JobExecutionException("fail to update the route_status of the route to 0 with routeNo="+routeNo);
            }
        }
        if(route.getRouteStatus()==2){
            List<MerchantRoutePriceInfo> merchantRoutePriceInfos=merchantRoutePriceInfoMapper.selectMerchantRouterPriceInfoByRouteNo(routeNo);

            Date lastDate=merchantRoutePriceInfos.get(0).getValidTime();
            for(int j=1;j<merchantRoutePriceInfos.size();j++){
                Date validDate=merchantRoutePriceInfos.get(j).getValidTime();
                if(validDate.after(lastDate)){
                    lastDate=validDate;
                }
            }
            Date currentDate=new Date();
            if(lastDate.before(currentDate)||lastDate.equals(currentDate)){
                Route routeUpdate=new Route();
                routeUpdate.setRouteNo(routeNo);
                routeUpdate.setUpdateTime(new Date());
                routeUpdate.setRouteStatus(4);
                if(routeMapper.updateRouteSelective(routeUpdate)<1){
                    throw new JobExecutionException("fail to update the route_status of the route to 0 with routeNo="+routeNo);
                }
            }
            if(lastDate.after(currentDate)){
                Map<String,String> dataMap=new HashMap<>();
                dataMap.put("jobName","job"+routeNo);
                dataMap.put("jobGroupName","jobGroupName"+TimerType.ROUTE_VALID_TASK.getName());
                dataMap.put("routeNo",String.valueOf(routeNo));
                try {
                    timerTaskManagement.addTimerTask(RouteDistributeMerchantTimerTask.class, TimerType.ROUTE_VALID_TASK.getName(), String.valueOf(routeNo), lastDate, dataMap);
                }catch (Exception e){
                    throw new JobExecutionException("fail to add route valid task with routeNo="+routeNo);
                }
            }


        }
        List<RouteDistributeMerchantInfo> routeDistributeMerchantInfoList=routeDistributeMerchantInfoMapper.selectRouteDistributeMerchantInfoByRouteNo(routeNo);

        if(routeDistributeMerchantInfoList!=null&&routeDistributeMerchantInfoList.size()!=0) {
            List<Integer> notHandleMerchantIdList = new ArrayList<>();
            for (int i = 0; i < routeDistributeMerchantInfoList.size(); i++) {
                RouteDistributeMerchantInfo routeDistributeMerchantInfo = routeDistributeMerchantInfoList.get(i);
                if (routeDistributeMerchantInfo.getHandleStatus() == 0) {
                    notHandleMerchantIdList.add(routeDistributeMerchantInfo.getId());
                }
            }
            if (notHandleMerchantIdList.size() > 0) {
                if (routeDistributeMerchantInfoMapper.updateStatusByList(notHandleMerchantIdList, 3, new Date()) < notHandleMerchantIdList.size()) {
                    throw new JobExecutionException("fail to update the handle_status of the routeDistributeMerchantInfo to 3 with routeNo=" + routeNo);
                }
            }
        }
    }
}
