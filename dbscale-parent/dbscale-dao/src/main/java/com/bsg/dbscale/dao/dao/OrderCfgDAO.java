package com.bsg.dbscale.dao.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bsg.dbscale.dao.domain.OrderCfgDO;

public interface OrderCfgDAO {

    List<OrderCfgDO> list(@Param("category") String category);

    OrderCfgDO get(@Param("category") String category, @Param("type") String type);
    
    int countByScale(@Param("type") String type, @Param("cpuCnt") Double cpuCnt, @Param("memSize") Double memSize); 

    int save(OrderCfgDO orderCfgDO);

    int update(OrderCfgDO orderCfgDO);

    int remove(String category);

}
