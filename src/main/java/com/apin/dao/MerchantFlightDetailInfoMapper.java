package com.apin.dao;


import com.apin.po.MerchantFlightDetailInfo;

import java.util.List;

/**
 * Created by Administrator on 2016/9/23.
 */
public interface MerchantFlightDetailInfoMapper {

    public List<MerchantFlightDetailInfo> selectMatchedMerchantFlightDetailInfoByJourneyId(long journeyId);
}
