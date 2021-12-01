package com.bsg.dbscale.dao.dao;

import org.apache.ibatis.annotations.Param;

import com.bsg.dbscale.dao.domain.GroupDO;
import com.bsg.dbscale.dao.query.GroupQuery;

public interface GroupDAO extends CrudDAO<GroupDO, GroupQuery> {

    int countByNameAndCreator(@Param("name") String name, @Param("creator") String creator);
}
