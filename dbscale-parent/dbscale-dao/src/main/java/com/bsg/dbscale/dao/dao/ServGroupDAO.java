package com.bsg.dbscale.dao.dao;

import com.bsg.dbscale.dao.domain.ServGroupDO;
import com.bsg.dbscale.dao.query.ServGroupQuery;

public interface ServGroupDAO extends CrudDAO<ServGroupDO, ServGroupQuery> {

    ServGroupDO getByOrderGroupId(String orderGroupId);
    
    ServGroupDO getByName(String name);
    
    int countBySubsystemId(String businessSubsystemId);
    
    int countByBusinessAreaId(String businessAreaId);
}
