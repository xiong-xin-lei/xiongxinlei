package com.bsg.dbscale.dao.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bsg.dbscale.dao.domain.DefServDO;

public interface DefServDAO {

    List<DefServDO> list(@Param("enabled") Boolean enabled);
    
    DefServDO get(String code);

}
