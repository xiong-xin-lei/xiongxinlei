package com.bsg.dbscale.service.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bsg.dbscale.dao.domain.AppDO;
import com.bsg.dbscale.dao.domain.RoleDO;
import com.bsg.dbscale.dao.domain.UserDO;
import com.bsg.dbscale.service.constant.Consts;
import com.bsg.dbscale.service.dto.AppDTO;

@Service
public class AppService extends BaseService {

    @Transactional
    public List<AppDTO> list(UserDO userDO, Long pid) throws Exception {
        List<AppDTO> appDTOs = new ArrayList<>();
        try {
            if (userDO != null) {
                RoleDO roleDO = userDO.getRole();
                List<AppDO> visiableAppDOs = new ArrayList<>();
                if (roleDO != null) {
                    if (!roleDO.getId().equals(Consts.ROLE_SYSTEM_SUPER_MANAGER)) {
                        visiableAppDOs = appDAO.listByPidAndRoleId(pid, roleDO.getId());
                    } else {
                        visiableAppDOs = appDAO.listByPid(pid);
                    }
                }
                for (AppDO appDO : visiableAppDOs) {
                    AppDTO appDTO = new AppDTO();
                    appDTOs.add(appDTO);
                    BeanUtils.copyProperties(appDO, appDTO);
                }
            }
            return appDTOs;
        } catch (Exception e) {
            throw new Exception(e);
        }

    }

}
