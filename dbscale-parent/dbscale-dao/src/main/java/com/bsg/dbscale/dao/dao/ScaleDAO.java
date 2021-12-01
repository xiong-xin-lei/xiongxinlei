package com.bsg.dbscale.dao.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bsg.dbscale.dao.domain.ScaleDO;
import com.bsg.dbscale.dao.query.ScaleQuery;

public interface ScaleDAO {

    List<ScaleDO> list(ScaleQuery scaleQuery);

    ScaleDO get(@Param("type") String type, @Param("cpuCnt") Double cpuCnt, @Param("memSize") Double memSize);

    int countByTypeAndName(@Param("type") String type, @Param("name") String name);

    int save(ScaleDO scaleDO);

    int updateEnabled(ScaleDO scaleDO);

    int remove(@Param("type") String type, @Param("cpuCnt") Double cpuCnt, @Param("memSize") Double memSize);

}
