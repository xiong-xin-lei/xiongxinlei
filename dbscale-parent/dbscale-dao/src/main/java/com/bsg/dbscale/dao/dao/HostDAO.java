package com.bsg.dbscale.dao.dao;

import com.bsg.dbscale.dao.domain.HostDO;
import com.bsg.dbscale.dao.query.HostQuery;

public interface HostDAO extends CrudDAO<HostDO, HostQuery> {
    
    HostDO getByIdOrIp(String idOrIp);
    
    HostDO getByRelateId(String relateId);
    
    int countByIp(String ip);

    int updateRelateId(HostDO hostDO);

}
