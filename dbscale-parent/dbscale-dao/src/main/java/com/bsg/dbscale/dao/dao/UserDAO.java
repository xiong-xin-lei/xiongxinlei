package com.bsg.dbscale.dao.dao;

import java.util.List;

import com.bsg.dbscale.dao.domain.UserDO;
import com.bsg.dbscale.dao.query.UserQuery;

public interface UserDAO extends CrudDAO<UserDO, UserQuery> {

    int updateEnabled(UserDO userDO);
    
    int updatePwd(UserDO userDO);
    
    List<UserDO> listBase();
    
}
