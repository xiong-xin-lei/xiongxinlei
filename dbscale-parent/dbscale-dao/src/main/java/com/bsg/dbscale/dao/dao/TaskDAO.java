package com.bsg.dbscale.dao.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bsg.dbscale.dao.domain.TaskDO;
import com.bsg.dbscale.dao.query.TaskQuery;

public interface TaskDAO extends CrudDAO<TaskDO, TaskQuery> {
    
    List<TaskDO> listRunning();
    
    List<TaskDO> listLatest(@Param("siteId") String siteId, @Param("objType") String objType);

    TaskDO getLatest(@Param("objType") String objType, @Param("objId") String objId,
            @Param("actionType") String actionType);

    int updateToStart(TaskDO taskDO);

    int updateToEnd(TaskDO taskDO);

    int removeCascadeByObjTypeAndObjId(@Param("objType") String objType, @Param("objId") String objId);
}
