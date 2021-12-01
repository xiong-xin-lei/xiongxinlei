package com.bsg.dbscale.service.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.bsg.dbscale.dao.domain.PrivilegeDO;
import com.bsg.dbscale.service.dto.PrivilegeDTO;

@Service
public class PrivilegeService extends BaseService {

    public Result list(Boolean enabled, Boolean global) throws Exception {
        List<PrivilegeDTO> privilegeDTOs = new ArrayList<>();

        List<PrivilegeDO> privilegeDOs = privilegeDAO.list(enabled, global);
        for (PrivilegeDO privilegeDO : privilegeDOs) {
            PrivilegeDTO privilegeDTO = getShowDTO(privilegeDO);
            privilegeDTOs.add(privilegeDTO);
        }

        return Result.success(privilegeDTOs);
    }

    private PrivilegeDTO getShowDTO(PrivilegeDO privilegeDO) {
        PrivilegeDTO privilegeDTO = new PrivilegeDTO();
        privilegeDTO.setCode(privilegeDO.getCode());
        privilegeDTO.setDescription(privilegeDO.getDescription());
        privilegeDTO.setEnabled(privilegeDO.getEnabled());
        privilegeDTO.setSequence(privilegeDO.getSequence());
        return privilegeDTO;
    }

}
