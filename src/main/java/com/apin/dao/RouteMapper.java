package com.apin.dao;


import com.apin.po.Route;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2016/9/22.
 */
public interface RouteMapper {
    public int updateRouteSelective(Route route);

    public Route getRouteInfoByRouteNo(long routeNo);
}
