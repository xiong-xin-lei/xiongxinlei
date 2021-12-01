package com.bsg.dbscale.service.check;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.bsg.dbscale.dao.domain.GroupDO;
import com.bsg.dbscale.service.form.GroupForm;

@Service
public class GroupCheck extends BaseCheck {

    public CheckResult checkSave(GroupForm groupForm, String activeUsername) {
        // Non-logical check for save
        CheckResult checkResult = checkSaveNonLogic(groupForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            return checkResult;
        }

        // logical check for save
        checkResult = checkSaveLogic(groupForm, activeUsername);

        return checkResult;
    }

    public CheckResult checkUpdate(String groupId, GroupForm groupForm, String activeUsername) {
        // Non-logical check for update
        CheckResult checkResult = checkUpdateNonLogic(groupForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            return checkResult;
        }

        // logical check for update
        checkResult = checkUpdateLogic(groupId, groupForm, activeUsername);

        return checkResult;
    }

    public CheckResult checkRemove(String groupId, String activeUsername) {
        GroupDO groupDO = groupDAO.get(groupId);
        if (groupDO == null) {
            String msg = "组不存在。";
            return CheckResult.failure(msg);
        }

        if (groupDO.getCreator().equals(activeUsername) && BooleanUtils.isTrue(groupDO.getSys())) {
            String msg = "禁止删除系统资源。";
            return CheckResult.failure(msg);
        }
        return CheckResult.success();
    }

    public CheckResult checkSaveUser(String groupId, String activeUsername) {
        GroupDO groupDO = groupDAO.get(groupId);
        if (groupDO == null) {
            String msg = "组不存在。";
            return CheckResult.failure(msg);
        }
        
        if (!groupDO.getCreator().equals(activeUsername)) {
            String msg = "只有该组的创建者才可以加人。";
            return CheckResult.failure(msg);
        }
        return CheckResult.success();
    }

    public CheckResult checkRemoveUser(String groupId) {
        GroupDO groupDO = groupDAO.get(groupId);
        if (groupDO == null) {
            String msg = "组不存在。";
            return CheckResult.failure(msg);
        }
        return CheckResult.success();
    }

    private CheckResult checkSaveNonLogic(GroupForm groupForm) {
        if (StringUtils.isBlank(groupForm.getName())) {
            String msg = "组名不能为空。";
            return CheckResult.failure(msg);
        }
        return CheckResult.success();
    }

    private CheckResult checkSaveLogic(GroupForm groupForm, String activeUsername) {
        int cnt = groupDAO.countByNameAndCreator(groupForm.getName(), activeUsername);
        if (cnt > 0) {
            String msg = "组名已存在。";
            return CheckResult.failure(msg);
        }
        return CheckResult.success();
    }

    private CheckResult checkUpdateNonLogic(GroupForm groupForm) {
        if (groupForm.getName() != null && StringUtils.isEmpty(groupForm.getName())) {
            String msg = "组名不能为空。";
            return CheckResult.failure(msg);
        }
        return CheckResult.success();
    }

    private CheckResult checkUpdateLogic(String groupId, GroupForm groupForm, String activeUsername) {
        GroupDO groupDO = groupDAO.get(groupId);
        if (groupDO == null) {
            String msg = "组不存在。";
            return CheckResult.failure(msg);
        }

        if (BooleanUtils.isTrue(groupDO.getSys())) {
            String msg = "禁止修改系统资源。";
            return CheckResult.failure(msg);
        }

        if (!groupDO.getCreator().equals(activeUsername)) {
            String msg = "只能修改自己创建的组。";
            return CheckResult.failure(msg);
        }

        if (StringUtils.isNotBlank(groupForm.getName()) && !groupForm.getName().equals(groupDO.getName())) {
            int cnt = groupDAO.countByNameAndCreator(groupForm.getName(), activeUsername);
            if (cnt > 0) {
                String msg = "组名已存在。";
                return CheckResult.failure(msg);
            }
        }
        return CheckResult.success();
    }
}
