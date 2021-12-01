package com.bsg.dbscale.dao.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bsg.dbscale.dao.domain.AppDO;

public interface AppDAO {

    List<AppDO> listByPid(@Param("pid") Long pid);
    
    List<AppDO> listByPidAndRoleId(@Param("pid") Long pid,@Param("roleId") String roleId);

    List<AppDO> listMenu();
}
