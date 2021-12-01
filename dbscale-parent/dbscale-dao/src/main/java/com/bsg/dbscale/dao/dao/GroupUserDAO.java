package com.bsg.dbscale.dao.dao;

import org.apache.ibatis.annotations.Param;

public interface GroupUserDAO {
    
    int countByGroupIdAndUsername(@Param("groupId") String groupId, @Param("username") String username);

    int save(@Param("groupId") String groupId, @Param("username") String username);

    int removeByGroupId(String groupId);

    int remove(@Param("groupId") String groupId, @Param("username") String username);
}
