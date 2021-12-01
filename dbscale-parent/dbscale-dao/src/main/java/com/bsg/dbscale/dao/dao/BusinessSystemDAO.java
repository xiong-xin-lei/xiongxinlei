package com.bsg.dbscale.dao.dao;

import org.apache.ibatis.annotations.Param;

import com.bsg.dbscale.dao.domain.BusinessSystemDO;
import com.bsg.dbscale.dao.query.BusinessSystemQuery;

public interface BusinessSystemDAO extends CrudDAO<BusinessSystemDO, BusinessSystemQuery> {
    
    BusinessSystemDO getByNameAndOwner(@Param("name") String name, @Param("owner") String owner);
    
    int updateEnabled(BusinessSystemDO businessSystemDO);

}
