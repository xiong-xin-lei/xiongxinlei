package com.bsg.dbscale.dao.dao;

import com.bsg.dbscale.dao.domain.UnitDO;

public interface UnitDAO {
    
    UnitDO get(String id);

    int save(UnitDO unitDO);
    
    int update(UnitDO unitDO);
    
    int remove(String id);
}
