package com.bsg.dbscale.dao.dao;

import java.util.List;

import com.bsg.dbscale.dao.domain.DictTypeDO;

public interface DictTypeDAO {

    List<DictTypeDO> list();

    DictTypeDO get(String code);
}
