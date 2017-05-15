package com.apin.timer;

/**
 * Created by Administrator on 2016/9/20.
 */
public enum TimerType {

    ORDER_TASK("orderTask"),
    ORDER_NOTIFY_TASK("orderNotifyTask"),
    ROUTE_DISTRIBUTE_MERCHANT_TASK("routeDistributeMerchantTask"),
    ROUTE_VALID_TASK("routeValidTask"),
    REST_PAY_NOTICE_TASK("restPayNoticeTask"),
    REST_PAY_TASK("restPayTask");

    private String name;

    TimerType(String name){
        this.name=name;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
