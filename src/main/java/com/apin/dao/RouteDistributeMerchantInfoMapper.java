package com.apin.dao;


import com.apin.po.RouteDistributeMerchantInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/9/22.
 */
public interface RouteDistributeMerchantInfoMapper {

    public List<RouteDistributeMerchantInfo> selectRouteDistributeMerchantInfoByRouteNo(long routeNo);

    public int updateStatusByList(@Param("idList") List<Integer> idList, @Param("handleStatus") int handleStatus, @Param("updateTime") Date updateTime);

}
