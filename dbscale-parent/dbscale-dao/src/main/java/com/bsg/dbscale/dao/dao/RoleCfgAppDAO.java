package com.bsg.dbscale.dao.dao;

import java.util.List;

import com.bsg.dbscale.dao.domain.RoleCfgAppDO;

/**
 * 
 * @author ZhuXH
 * @date 2019年5月17日
 */
public interface RoleCfgAppDAO {

	List<Long> listAppIdByRoleId(String roleId);
	
	int save(RoleCfgAppDO cfgAppDO);
	
	int removeByRoleId(String roleId);
}
