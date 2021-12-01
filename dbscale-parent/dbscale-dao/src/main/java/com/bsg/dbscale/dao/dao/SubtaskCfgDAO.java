package com.bsg.dbscale.dao.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bsg.dbscale.dao.domain.SubtaskCfgDO;

public interface SubtaskCfgDAO {

    List<SubtaskCfgDO> list();

    SubtaskCfgDO get(@Param("objType") String objType, @Param("actionType") String actionType);

    int update(SubtaskCfgDO subtaskCfgDO);
}
