package com.bsg.dbscale.dao.dao;

import org.apache.ibatis.annotations.Param;

import com.bsg.dbscale.dao.domain.ServDO;

public interface ServDAO {

    ServDO get(String id);

    int countByScale(@Param("type") String type, @Param("cpuCnt") Double cpuCnt, @Param("memSize") Double memSize);

    int save(ServDO servDO);

    int update(ServDO servDO);

    int updateRelateId(ServDO servDO);

    int updateMonitorFlag(ServDO servDO);

    int remove(String id);

}
