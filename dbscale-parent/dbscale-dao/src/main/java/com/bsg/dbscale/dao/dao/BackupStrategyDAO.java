package com.bsg.dbscale.dao.dao;

import com.bsg.dbscale.dao.domain.BackupStrategyDO;
import com.bsg.dbscale.dao.query.BackupStrategyQuery;

public interface BackupStrategyDAO extends CrudDAO<BackupStrategyDO, BackupStrategyQuery> {

    int updateEnabled(BackupStrategyDO backupStrategyDO);
    
    int removeByServGroupId(String servGroupId);

}
