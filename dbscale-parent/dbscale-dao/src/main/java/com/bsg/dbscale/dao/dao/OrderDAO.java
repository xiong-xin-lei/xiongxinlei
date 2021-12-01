package com.bsg.dbscale.dao.dao;

import org.apache.ibatis.annotations.Param;

import com.bsg.dbscale.dao.domain.OrderDO;

public interface OrderDAO {

    int countByScale(@Param("type") String type, @Param("cpuCnt") Double cpuCnt, @Param("memSize") Double memSize);

    int save(OrderDO orderDO);

    int update(OrderDO orderDO);

    int removeByOrderGroupId(@Param("orderGroupId") String orderGroupId);

}
