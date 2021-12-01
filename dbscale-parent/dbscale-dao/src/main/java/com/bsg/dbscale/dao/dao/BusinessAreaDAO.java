package com.bsg.dbscale.dao.dao;

import org.apache.ibatis.annotations.Param;

import com.bsg.dbscale.dao.domain.BusinessAreaDO;
import com.bsg.dbscale.dao.query.BusinessAreaQuery;

public interface BusinessAreaDAO extends CrudDAO<BusinessAreaDO, BusinessAreaQuery> {

    BusinessAreaDO getByNameAndSiteId(@Param("name") String name, @Param("siteId") String siteId);

    int updateEnabled(BusinessAreaDO businessAreaDO);

}
