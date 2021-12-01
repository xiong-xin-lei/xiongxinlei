package com.bsg.dbscale.service.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bsg.dbscale.dao.domain.DictDO;
import com.bsg.dbscale.dao.domain.DictTypeDO;
import com.bsg.dbscale.dao.domain.RoleDO;
import com.bsg.dbscale.dao.domain.UserDO;
import com.bsg.dbscale.service.check.CheckResult;
import com.bsg.dbscale.service.check.RoleCheck;
import com.bsg.dbscale.service.constant.Consts;
import com.bsg.dbscale.service.constant.DictTypeConsts;
import com.bsg.dbscale.service.dto.DisplayDTO;
import com.bsg.dbscale.service.dto.InfoDTO;
import com.bsg.dbscale.service.dto.RoleDTO;
import com.bsg.dbscale.service.form.RoleForm;
import com.bsg.dbscale.service.util.PrimaryKeyUtil;
import com.bsg.dbscale.util.DateUtils;

@Service
public class RoleService extends BaseService {

    @Autowired
    private RoleCheck roleCheck;

    @Transactional
    public Result list(String activeUsername) {
        List<RoleDTO> roleDTOs = new ArrayList<>();
        UserDO activeUserDO = userDAO.get(activeUsername);
        if (activeUserDO != null) {
            RoleDO curRoleDO = activeUserDO.getRole();
            if (curRoleDO != null && curRoleDO.getId().equals(Consts.ROLE_SYSTEM_SUPER_MANAGER)) {
                List<RoleDO> roleDOs = roleDAO.list(null);
                List<UserDO> userDOs = userDAO.list(null);
                List<DictTypeDO> dictTypeDOs = dictTypeDAO.list();
                for (RoleDO roleDO : roleDOs) {
                    RoleDTO roleDTO = getShowDTO(roleDO, userDOs, dictTypeDOs);
                    roleDTOs.add(roleDTO);
                }
            }
        }

        return Result.success(roleDTOs);
    }

    public Result get(String roleId) {
        RoleDTO roleDTO = null;
        RoleDO roleDO = roleDAO.get(roleId);
        if (roleDO != null) {
            List<UserDO> userDOs = userDAO.list(null);
            List<DictTypeDO> dictTypeDOs = dictTypeDAO.list();
            roleDTO = getShowDTO(roleDO, userDOs, dictTypeDOs);
        }

        return Result.success(roleDTO);
    }

    @Transactional(rollbackFor = Exception.class)
    public Result save(RoleForm roleForm, String activeUsername) {
        CheckResult checkResult = roleCheck.checkSave(roleForm, activeUsername);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        RoleDO roleDO = buildRoleDOForSave(roleForm, activeUsername);
        roleDAO.save(roleDO);

        return Result.success();
    }

    @Transactional(rollbackFor = Exception.class)
    public Result update(String roleId, RoleForm roleForm, String activeUsername) {
        CheckResult checkResult = roleCheck.checkUpdate(roleId, roleForm, activeUsername);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        RoleDO roleDO = buildRoleDOForUpdate(roleId, roleForm);
        roleDAO.update(roleDO);

        return Result.success();
    }

    @Transactional(rollbackFor = Exception.class)
    public Result remove(String roleId, String activeUsername) {
        CheckResult checkResult = roleCheck.checkRemove(roleId, activeUsername);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        roleDAO.remove(roleId);

        return Result.success();
    }

    private RoleDTO getShowDTO(RoleDO roleDO, List<UserDO> userDOs, List<DictTypeDO> dictTypeDOs) {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(roleDO.getId());
        roleDTO.setName(roleDO.getName());
        roleDTO.setDescription(roleDO.getDescription());
        roleDTO.setManager(roleDO.getManager());

        DisplayDTO dataScopeDisplayDTO = new DisplayDTO();
        roleDTO.setDataScope(dataScopeDisplayDTO);
        dataScopeDisplayDTO.setCode(roleDO.getDataScope());
        DictDO dataScopeDictDO = findDictDO(dictTypeDOs, DictTypeConsts.DATA_SCOPE, roleDO.getDataScope());
        if (dataScopeDictDO != null) {
            dataScopeDisplayDTO.setDisplay(dataScopeDictDO.getName());
        }

        roleDTO.setSys(roleDO.getSys());
        InfoDTO createdDTO = new InfoDTO();
        roleDTO.setCreated(createdDTO);
        createdDTO.setUsername(roleDO.getCreator());
        UserDO createdUserDO = findUserDO(userDOs, roleDO.getCreator());
        if (createdUserDO != null) {
            createdDTO.setName(createdUserDO.getName());
        }
        createdDTO.setTimestamp(DateUtils.dateTimeToString(roleDO.getGmtCreate()));
        return roleDTO;
    }

    private RoleDO buildRoleDOForSave(RoleForm roleForm, String username) {
        RoleDO roleDO = new RoleDO();
        roleDO.setId(PrimaryKeyUtil.get());
        roleDO.setName(roleForm.getName());
        roleDO.setDescription(StringUtils.trimToEmpty(roleForm.getDescription()));
        roleDO.setManager(roleForm.getManager());
        roleDO.setDataScope(roleForm.getDataScope());
        roleDO.setSys(false);
        roleDO.setCreator(username);
        roleDO.setGmtCreate(systemDAO.getCurrentSqlDateTime());
        return roleDO;
    }

    private RoleDO buildRoleDOForUpdate(String roleId, RoleForm roleForm) {
        RoleDO roleDO = roleDAO.get(roleId);

        if (roleForm.getName() != null) {
            roleDO.setName(roleForm.getName());
        }
        if (roleForm.getDescription() != null) {
            roleDO.setDescription(roleForm.getDescription());
        }
        if (roleForm.getManager() != null) {
            roleDO.setManager(roleForm.getManager());
        }
        if (roleForm.getDataScope() != null) {
            roleDO.setDataScope(roleForm.getDataScope());
        }
        return roleDO;
    }
    
    public ObjModel getObjModel(String roleId) {
        RoleDO roleDO = roleDAO.get(roleId);
        if (roleDO != null) {
            return new ObjModel(roleDO.getName(), null);
        }
        return null;
    }

}
