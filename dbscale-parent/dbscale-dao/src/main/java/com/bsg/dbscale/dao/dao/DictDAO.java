package com.bsg.dbscale.dao.dao;

import org.apache.ibatis.annotations.Param;

import com.bsg.dbscale.dao.domain.DictDO;

public interface DictDAO {

    DictDO get(@Param("dictTypeCode") String dictTypeCode, @Param("code") String code);
    
    int countByNameAndDictTypeCode(@Param("name") String name, @Param("dictTypeCode") String dictTypeCode);

    int update(DictDO dictDO);
}
