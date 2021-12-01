package com.bsg.dbscale.dao.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bsg.dbscale.dao.domain.PrivilegeDO;

public interface PrivilegeDAO {

    List<PrivilegeDO> list(@Param("enabled") Boolean enabled, @Param("global") Boolean global);

}
