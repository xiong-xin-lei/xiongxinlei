package com.bsg.dbscale.dao.dao;

import java.util.List;

import com.bsg.dbscale.dao.domain.ForceRebuildLogDO;
import com.bsg.dbscale.dao.query.ForceRebuildLogQuery;

public interface ForceRebuildLogDAO {

    List<ForceRebuildLogDO> list(ForceRebuildLogQuery forceRebuildLogQuery);

    int save(ForceRebuildLogDO forceRebuildLogDO);

    int update(ForceRebuildLogDO forceRebuildLogDO);
    
    int removeByHostRelateId(String hostRelateId);
    
    int removeByUnitRelateId(String unitRelateId);

}
