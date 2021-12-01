package com.bsg.dbscale.dao.dao;

import org.apache.ibatis.annotations.Param;

import com.bsg.dbscale.dao.domain.BusinessSubsystemDO;
import com.bsg.dbscale.dao.query.BusinessSubsystemQuery;

public interface BusinessSubsystemDAO extends CrudDAO<BusinessSubsystemDO, BusinessSubsystemQuery> {

    BusinessSubsystemDO getByNameAndSystemId(@Param("name") String name,
            @Param("businessSystemId") String businessSystemId);

    int updateEnabled(BusinessSubsystemDO businessSubsystemDO);

}
