package com.bsg.dbscale.service.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import com.bsg.dbscale.dao.domain.GroupDO;
import com.bsg.dbscale.dao.domain.RoleDO;
import com.bsg.dbscale.dao.domain.UserDO;
import com.bsg.dbscale.service.check.CheckResult;
import com.bsg.dbscale.service.check.UserCheck;
import com.bsg.dbscale.service.constant.Consts;
import com.bsg.dbscale.service.constant.DictConsts;
import com.bsg.dbscale.service.dto.IdentificationDTO;
import com.bsg.dbscale.service.dto.InfoDTO;
import com.bsg.dbscale.service.dto.UserDTO;
import com.bsg.dbscale.service.form.CheckForm;
import com.bsg.dbscale.service.form.GroupForm;
import com.bsg.dbscale.service.form.PwdForm;
import com.bsg.dbscale.service.form.UserForm;
import com.bsg.dbscale.service.query.UserQuery;
import com.bsg.dbscale.util.DateUtils;

@Service
public class UserService extends BaseService {

    @Autowired
    private UserCheck userCheck;

    @Autowired
    private GroupService groupService;

    public Result list(UserQuery userQuery, String activeUsername) throws Exception {
        List<UserDTO> userDTOs = new ArrayList<>();
        UserDO activeUserDO = userDAO.get(activeUsername);
        if (activeUserDO != null) {
            List<UserDO> userDOs = listQualifiedData(userQuery, activeUsername);
            for (UserDO userDO : userDOs) {
                UserDTO userDTO = getShowDTO(userDO, userDOs);
                userDTOs.add(userDTO);
            }
        }

        return Result.success(userDTOs);
    }

    public List<UserDO> listQualifiedData(UserQuery userQuery, String activeUsername) {
        List<UserDO> results = new ArrayList<>();
        com.bsg.dbscale.dao.query.UserQuery daoQuery = convertToDAOQuery(userQuery);
        List<UserDO> userDOs = userDAO.list(daoQuery);
        Set<String> usernames = listVisiableUserData(activeUsername);
        for (UserDO userDO : userDOs) {
            if (usernames.contains(userDO.getUsername())) {
                results.add(userDO);
            }
        }
        return results;
    }

    public Result get(String username) {
        UserDTO userDTO = null;
        UserDO userDO = userDAO.get(username);
        if (userDO != null) {
            List<UserDO> userDOs = userDAO.list(null);
            userDTO = getShowDTO(userDO, userDOs);
        }

        return Result.success(userDTO);
    }

    @Transactional(rollbackFor = Exception.class)
    public Result save(UserForm userForm, String activeUsername) {
        CheckResult checkResult = userCheck.checkSave(userForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }
        Date nowDate = systemDAO.getCurrentSqlDateTime();
        UserDO userDO = buildUserDOForSave(userForm, nowDate, activeUsername);

        GroupForm groupForm = new GroupForm();
        groupForm.setName(userDO.getUsername());
        GroupDO groupDO = groupService.buildGroupDOForSave(groupForm, nowDate, userDO.getUsername());

        userDAO.save(userDO);
        groupDAO.save(groupDO);
        groupUserDAO.save(groupDO.getId(), userDO.getUsername());

        return Result.success();
    }

    @Transactional(rollbackFor = Exception.class)
    public Result update(String username, UserForm userForm, String activeUsername) {
        CheckResult checkResult = userCheck.checkUpdate(username, userForm, activeUsername);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        Date nowDate = systemDAO.getCurrentSqlDateTime();
        UserDO userDO = buildUserDOForUpdate(username, userForm, nowDate, activeUsername);

        userDAO.update(userDO);

        return Result.success();
    }

    @Transactional(rollbackFor = Exception.class)
    public Result enabled(String username, boolean enabled, String activeUsername) {

        CheckResult checkResult = userCheck.checkEnabled(username, enabled);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        UserDO userDO = userDAO.get(username);
        userDO.setEnabled(enabled);
        userDO.setEditor(activeUsername);

        Date date = systemDAO.getCurrentSqlDateTime();
        userDO.setGmtModified(date);

        userDAO.updateEnabled(userDO);

        return Result.success();

    }

    @Transactional(rollbackFor = Exception.class)
    public Result updatePwd(String username, PwdForm pwdForm) {
        CheckResult checkResult = userCheck.checkUpdatePwd(username, pwdForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        UserDO userDO = userDAO.get(username);
        String password = DigestUtils.md5DigestAsHex(pwdForm.getNewPwd().getBytes());
        userDO.setPassword(password);
        userDO.setEditor(username);

        Date date = systemDAO.getCurrentSqlDateTime();
        userDO.setGmtModified(date);

        userDAO.updatePwd(userDO);

        return Result.success();
    }

    @Transactional(rollbackFor = Exception.class)
    public Result check(String username, CheckForm checkForm) {
        CheckResult checkResult = userCheck.check(username, checkForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }
        return Result.success();
    }

    private com.bsg.dbscale.dao.query.UserQuery convertToDAOQuery(UserQuery userQuery) {
        com.bsg.dbscale.dao.query.UserQuery daoQuery = new com.bsg.dbscale.dao.query.UserQuery();
        daoQuery.setGroupId(userQuery.getGroupId());
        daoQuery.setRoleId(userQuery.getRoleId());
        daoQuery.setEnabled(userQuery.getEnabled());
        return daoQuery;
    }

    private UserDTO getShowDTO(UserDO userDO, List<UserDO> userDOs) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(userDO.getUsername());
        userDTO.setName(userDO.getName());
        userDTO.setTelephone(userDO.getTelephone());
        userDTO.setEmail(userDO.getEmail());
        userDTO.setCompany(userDO.getCompany());
        userDTO.setEmerContact(userDO.getEmerContact());
        userDTO.setEmerTel(userDO.getEmerTel());
        userDTO.setAuthType(userDO.getAuthType());
        userDTO.setEnabled(userDO.getEnabled());
        userDTO.setOgAutoExamine(userDO.getOgAutoExamine());
        userDTO.setOgAutoExecute(userDO.getOgAutoExecute());

        IdentificationDTO roleDTO = new IdentificationDTO();
        userDTO.setRole(roleDTO);

        RoleDO roleDO = userDO.getRole();
        roleDTO.setId(roleDO.getId());
        roleDTO.setName(roleDO.getName());

        List<IdentificationDTO> groupDTOs = new ArrayList<>();
        userDTO.setGroups(groupDTOs);

        List<GroupDO> groupDOs = userDO.getGroups();
        for (GroupDO groupDO : groupDOs) {
            IdentificationDTO groupDTO = new IdentificationDTO();
            groupDTO.setId(groupDO.getId());
            groupDTO.setName(groupDO.getName());
            groupDTOs.add(groupDTO);
        }

        InfoDTO createdDTO = new InfoDTO();
        userDTO.setCreated(createdDTO);
        createdDTO.setUsername(userDO.getCreator());
        UserDO createdUserDO = findUserDO(userDOs, userDO.getCreator());
        if (createdUserDO != null) {
            createdDTO.setName(createdUserDO.getName());
        }
        createdDTO.setTimestamp(DateUtils.dateTimeToString(userDO.getGmtCreate()));

        return userDTO;
    }

    private UserDO buildUserDOForSave(UserForm userForm, Date nowDate, String activeUsername) {
        UserDO userDO = new UserDO();
        userDO.setUsername(userForm.getUsername());
        String password = DigestUtils.md5DigestAsHex(userForm.getPassword().getBytes());
        userDO.setPassword(password);
        userDO.setName(userForm.getName());
        userDO.setTelephone(userForm.getTelephone());
        userDO.setEmail(userForm.getEmail());
        userDO.setCompany(userForm.getCompany());
        userDO.setEmerContact(userForm.getEmerContact());
        userDO.setEmerTel(userForm.getEmerTel());
        userDO.setAuthType(DictConsts.AUTH_TYPE_NATIVE);
        userDO.setEnabled(true);
        if (userForm.getRoleId() == null) {
            userDO.setRoleId(Consts.ROLE_SYSTEM_USER);
        } else {
            userDO.setRoleId(userForm.getRoleId());
        }
        userDO.setOgAutoExamine(BooleanUtils.isTrue(userForm.getOgAutoExamine()));
        userDO.setOgAutoExecute(BooleanUtils.isTrue(userForm.getOgAutoExecute()));
        userDO.setCreator(activeUsername);
        userDO.setGmtCreate(nowDate);
        return userDO;
    }

    private UserDO buildUserDOForUpdate(String username, UserForm userForm, Date nowDate, String activeUsername) {
        UserDO userDO = userDAO.get(username);
        if (StringUtils.isNotBlank(userForm.getName())) {
            userDO.setName(userForm.getName());
        }
        if (StringUtils.isNotBlank(userForm.getTelephone())) {
            userDO.setTelephone(userForm.getTelephone());
        }
        if (StringUtils.isNotBlank(userForm.getEmail())) {
            userDO.setEmail(userForm.getEmail());
        }
        if (StringUtils.isNotBlank(userForm.getCompany())) {
            userDO.setCompany(userForm.getCompany());
        }
        if (StringUtils.isNotBlank(userForm.getEmerContact())) {
            userDO.setEmerContact(userForm.getEmerContact());
        }
        if (StringUtils.isNotBlank(userForm.getEmerTel())) {
            userDO.setEmerTel(userForm.getEmerTel());
        }
        if (userForm.getRoleId() != null && !userDO.getRole().getId().equals(userForm.getRoleId())) {
            userDO.setRoleId(userForm.getRoleId());
        }
        if (userForm.getOgAutoExamine() != null) {
            userDO.setOgAutoExamine(userForm.getOgAutoExamine());
        }
        if (userForm.getOgAutoExecute() != null) {
            userDO.setOgAutoExecute(userForm.getOgAutoExecute());
        }
        userDO.setEditor(activeUsername);
        userDO.setGmtModified(nowDate);
        return userDO;
    }

    public ObjModel getObjModel(String username) {
        UserDO userDO = userDAO.get(username);
        if (userDO != null) {
            String name = userDO.getName() + "(" + userDO.getUsername() + ")";
            return new ObjModel(name, null);
        }
        return null;
    }
}
