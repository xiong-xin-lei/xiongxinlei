package com.bsg.dbscale.dao.dao;

import com.bsg.dbscale.dao.domain.CrontabCfgDO;

public interface CrontabCfgDAO {

    CrontabCfgDO get(String triggerName);
}
