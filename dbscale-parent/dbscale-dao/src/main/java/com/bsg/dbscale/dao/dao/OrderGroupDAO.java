package com.bsg.dbscale.dao.dao;

import com.bsg.dbscale.dao.domain.OrderGroupDO;
import com.bsg.dbscale.dao.query.OrderGroupQuery;

public interface OrderGroupDAO extends CrudDAO<OrderGroupDO, OrderGroupQuery> {
    
    int countBySubsystemId(String businessSubsystemId);
    
    int countByName(String name);
    
    int countByBusinessAreaId(String businessAreaId);

    int updateStateAndMsg(OrderGroupDO orderGroupDO);
    
    int removeCascadeByName(String name);

}
