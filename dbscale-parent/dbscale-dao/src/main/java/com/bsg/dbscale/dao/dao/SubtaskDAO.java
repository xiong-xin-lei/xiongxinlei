package com.bsg.dbscale.dao.dao;

import com.bsg.dbscale.dao.domain.SubtaskDO;

public interface SubtaskDAO {
    
    SubtaskDO get(String id);
    
    int save(SubtaskDO subtaskDO);
    
    int updateToStart(SubtaskDO subtaskDO);
    
    int updateToEnd(SubtaskDO subtaskDO);
    
    int removeByTaskId(String taskId);
}
