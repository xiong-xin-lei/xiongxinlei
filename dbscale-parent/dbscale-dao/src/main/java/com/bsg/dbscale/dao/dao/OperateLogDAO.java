package com.bsg.dbscale.dao.dao;

import java.util.List;

import com.bsg.dbscale.dao.domain.OperateLogDO;
import com.bsg.dbscale.dao.query.OperateLogQuery;

public interface OperateLogDAO {

    List<OperateLogDO> list(OperateLogQuery operateLogQuery);

    int save(OperateLogDO operateLogDO);

}
