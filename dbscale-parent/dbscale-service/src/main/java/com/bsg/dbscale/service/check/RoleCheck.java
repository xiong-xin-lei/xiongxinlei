package com.bsg.dbscale.service.check;

import java.util.List;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.bsg.dbscale.dao.domain.DictDO;
import com.bsg.dbscale.dao.domain.RoleDO;
import com.bsg.dbscale.dao.domain.UserDO;
import com.bsg.dbscale.dao.query.UserQuery;
import com.bsg.dbscale.service.constant.DictTypeConsts;
import com.bsg.dbscale.service.form.RoleForm;

@Service
public class RoleCheck extends BaseCheck {

    public CheckResult checkSave(RoleForm roleForm, String activeUsername) {
        // Non-logical check for save
        CheckResult checkResult = checkSaveNonLogic(roleForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            return checkResult;
        }

        // logical check for save
        checkResult = checkSaveLogic(roleForm, activeUsername);

        return checkResult;
    }

    public CheckResult checkUpdate(String roleId, RoleForm roleForm, String activeUsername) {
        // Non-logical check for update
        CheckResult checkResult = checkUpdateNonLogic(roleForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            return checkResult;
        }

        // logical check for update
        checkResult = checkUpdateLogic(roleId, roleForm, activeUsername);

        return checkResult;
    }

    public CheckResult checkRemove(String roleId, String activeUsername) {
        UserDO activeUserDO = userDAO.get(activeUsername);
        if (activeUserDO == null) {
            String msg = "非法操作。";
            return CheckResult.failure(msg);
        }

        RoleDO roleDO = roleDAO.get(roleId);
        if (roleDO == null) {
            String msg = "角色不存在。";
            return CheckResult.failure(msg);
        }

        if (BooleanUtils.isTrue(roleDO.getSys())) {
            String msg = "禁止删除系统权限。";
            return CheckResult.failure(msg);
        }

        UserQuery query = new UserQuery();
        query.setRoleId(roleId);
        List<UserDO> userDOs = userDAO.list(query);
        if (userDOs.size() > 0) {
            String msg = "存在多个用户属于该角色，无法删除。";
            return CheckResult.failure(msg);
        }
        return CheckResult.success();
    }

    private CheckResult checkSaveNonLogic(RoleForm roleForm) {
        if (StringUtils.isBlank(roleForm.getName())) {
            String msg = "角色名不能为空。";
            return CheckResult.failure(msg);
        }

        if (roleForm.getManager() == null) {
            String msg = "是否为管理角色不能为空。";
            return CheckResult.failure(msg);
        }

        if (StringUtils.isBlank(roleForm.getDataScope())) {
            String msg = "可见数据范围不能为空。";
            return CheckResult.failure(msg);
        }
        return CheckResult.success();
    }

    private CheckResult checkSaveLogic(RoleForm roleForm, String activeUsername) {
        UserDO activeUserDO = userDAO.get(activeUsername);
        if (activeUserDO == null) {
            String msg = "非法操作。";
            return CheckResult.failure(msg);
        }

        int nameCnt = roleDAO.countByName(roleForm.getName());
        if (nameCnt != 0) {
            String msg = "角色名已存在。";
            return CheckResult.failure(msg);
        }

        DictDO dictDO = dictDAO.get(DictTypeConsts.DATA_SCOPE, roleForm.getDataScope());
        if (dictDO == null) {
            String msg = "可见数据范围不存在。";
            return CheckResult.failure(msg);
        }
        return CheckResult.success();
    }

    private CheckResult checkUpdateNonLogic(RoleForm roleForm) {
        if (roleForm.getName() != null && StringUtils.isEmpty(roleForm.getName())) {
            String msg = "角色名不能为空。";
            return CheckResult.failure(msg);
        }
        if (roleForm.getDataScope() != null && StringUtils.isEmpty(roleForm.getDataScope())) {
            String msg = "可见数据范围不能为空。";
            return CheckResult.failure(msg);
        }
        return CheckResult.success();
    }

    private CheckResult checkUpdateLogic(String roleId, RoleForm roleForm, String activeUsername) {
        UserDO activeUserDO = userDAO.get(activeUsername);
        if (activeUserDO == null) {
            String msg = "非法操作。";
            return CheckResult.failure(msg);
        }

        RoleDO roleDO = roleDAO.get(roleId);
        if (roleDO == null) {
            String msg = "角色不存在。";
            return CheckResult.failure(msg);
        }

        if (StringUtils.isNotBlank(roleForm.getName()) && !roleForm.getName().equals(roleDO.getName())) {
            int nameCnt = roleDAO.countByName(roleForm.getName());
            if (nameCnt != 0) {
                String msg = "角色名已存在。";
                return CheckResult.failure(msg);
            }
        }

        if (StringUtils.isNotBlank(roleForm.getDataScope()) && !roleForm.getDataScope().equals(roleDO.getDataScope())) {
            DictDO dictDO = dictDAO.get(DictTypeConsts.DATA_SCOPE, roleForm.getDataScope());
            if (dictDO == null) {
                String msg = "可见数据范围不存在。";
                return CheckResult.failure(msg);
            }
        }
        return CheckResult.success();
    }
}
