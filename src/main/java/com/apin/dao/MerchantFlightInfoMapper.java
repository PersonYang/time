package com.apin.dao;

import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/20.
 */
public interface MerchantFlightInfoMapper {

    public int increaseMerchantFlightInfoRemainTickets(Map<String, Object> map);
}
