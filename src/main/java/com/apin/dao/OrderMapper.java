package com.apin.dao;


import com.apin.po.Order;

import java.util.List;

/**
 * Created by Administrator on 2016/9/20.
 */
public interface OrderMapper {

    public Order selectOrderByOrderNo(long orderNo);

    public int updateOrderSelective(Order order);

}
