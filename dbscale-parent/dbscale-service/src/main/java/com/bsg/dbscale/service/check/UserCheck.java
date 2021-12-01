package com.bsg.dbscale.service.check;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.bsg.dbscale.dao.domain.RoleDO;
import com.bsg.dbscale.dao.domain.UserDO;
import com.bsg.dbscale.service.constant.DictConsts;
import com.bsg.dbscale.service.form.CheckForm;
import com.bsg.dbscale.service.form.PwdForm;
import com.bsg.dbscale.service.form.UserForm;

@Service
public class UserCheck extends BaseCheck {

    public static final String PW_PATTERN = "^(?=.*[0-9].*)(?=.*[A-Za-z].*).{8,}$";

    public CheckResult checkSave(UserForm userForm) {
        CheckResult checkResult = checkSaveNonLogic(userForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            return checkResult;
        }

        checkResult = checkSaveLogic(userForm);
        return checkResult;
    }

    public CheckResult checkUpdate(String username, UserForm userForm, String activeUsername) {
        CheckResult checkResult = checkUpdateNonLogic(userForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            return checkResult;
        }

        checkResult = checkUpdateLogic(username, userForm, activeUsername);

        return checkResult;
    }

    public CheckResult checkEnabled(String username, boolean enable) {
        UserDO userDO = userDAO.get(username);
        if (userDO == null) {
            String msg = "用户名不存在。";
            return CheckResult.failure(msg);
        }
        return CheckResult.success();
    }

    public CheckResult checkUpdatePwd(String username, PwdForm pwdForm) {
        UserDO userDO = userDAO.get(username);
        if (userDO == null) {
            String msg = "用户名不存在。";
            return CheckResult.failure(msg);
        }

        if (!userDO.getAuthType().equals(DictConsts.AUTH_TYPE_NATIVE)) {
            String msg = "禁止修改非本地用户密码。";
            return CheckResult.failure(msg);
        }

        if (StringUtils.isBlank(pwdForm.getOriginalPwd())) {
            String msg = "原密码不能为空。";
            return CheckResult.failure(msg);
        }

        if (StringUtils.isBlank(pwdForm.getNewPwd())) {
            String msg = "新密码不能为空。";
            return CheckResult.failure(msg);
        }
        if (!pwdForm.getNewPwd().matches(PW_PATTERN)) {
            String msg = "密码必须为8位以上且包含数字和字母。";
            return CheckResult.failure(msg);
        }

        if (pwdForm.getOriginalPwd().equals(pwdForm.getNewPwd())) {
            String msg = "新密码不能和原密码一致。";
            return CheckResult.failure(msg);
        }

        if (!userDO.getPassword().equals(DigestUtils.md5DigestAsHex(pwdForm.getOriginalPwd().getBytes()))) {
            String msg = "原密码不正确。";
            return CheckResult.failure(msg);
        }

        return CheckResult.success();
    }

    public CheckResult check(String username, CheckForm checkForm) {
        if (StringUtils.isBlank(username)) {
            String msg = "用户名不能为空。";
            return CheckResult.failure(msg);
        }

        if (StringUtils.isBlank(checkForm.getPwd())) {
            String msg = "密码不能为空。";
            return CheckResult.failure(msg);
        }

        String pwd = DigestUtils.md5DigestAsHex(checkForm.getPwd().getBytes());
        UserDO userDO = userDAO.get(username);
        if (userDO == null) {
            String msg = "用户名不存在。";
            return CheckResult.failure(msg);
        }

        if (!userDO.getPassword().equals(pwd)) {
            String msg = "密码错误。";
            return CheckResult.failure(msg);
        }
        return CheckResult.success();
    }

    private CheckResult checkSaveNonLogic(UserForm userForm) {
        String username = userForm.getUsername();
        if (StringUtils.isBlank(username)) {
            String msg = "用户名不能为空。";
            return CheckResult.failure(msg);
        }

        String password = userForm.getPassword();
        if (StringUtils.isBlank(password)) {
            String msg = "密码不能为空。";
            return CheckResult.failure(msg);
        }
        if (!password.matches(PW_PATTERN)) {
            String msg = "密码必须为8位以上且包含数字和字母。";
            return CheckResult.failure(msg);
        }

        if (StringUtils.isBlank(userForm.getName())) {
            String msg = "用户姓名不能为空。";
            return CheckResult.failure(msg);
        }

        if (StringUtils.isBlank(userForm.getTelephone())) {
            String msg = "联系方式不能为空。";
            return CheckResult.failure(msg);
        }

        if (StringUtils.isBlank(userForm.getEmail())) {
            String msg = "邮件不能为空。";
            return CheckResult.failure(msg);
        }

        if (StringUtils.isBlank(userForm.getCompany())) {
            String msg = "公司不能为空。";
            return CheckResult.failure(msg);
        }

        if (StringUtils.isBlank(userForm.getEmerContact())) {
            String msg = "紧急联系人不能为空。";
            return CheckResult.failure(msg);
        }

        if (StringUtils.isBlank(userForm.getEmerTel())) {
            String msg = "紧急联系人联系方式不能为空。";
            return CheckResult.failure(msg);
        }

        return CheckResult.success();
    }

    private CheckResult checkSaveLogic(UserForm userForm) {
        UserDO userDO = userDAO.get(userForm.getUsername());
        if (userDO != null) {
            String msg = "改用户已存在。";
            return CheckResult.failure(msg);
        }

        if (userForm.getRoleId() != null) {
            RoleDO roleDO = roleDAO.get(userForm.getRoleId());
            if (roleDO == null) {
                String msg = "角色信息不存在。";
                return CheckResult.failure(msg);
            }
        }

        return CheckResult.success();
    }

    private CheckResult checkUpdateNonLogic(UserForm userForm) {
        if (userForm.getName() != null && StringUtils.isEmpty(userForm.getName())) {
            String msg = "用户姓名不能为空。";
            return CheckResult.failure(msg);
        }

        if (userForm.getTelephone() != null && StringUtils.isEmpty(userForm.getTelephone())) {
            String msg = "联系方式不能为空。";
            return CheckResult.failure(msg);
        }

        if (userForm.getEmail() != null && StringUtils.isEmpty(userForm.getEmail())) {
            String msg = "邮件不能为空。";
            return CheckResult.failure(msg);
        }

        if (userForm.getCompany() != null && StringUtils.isEmpty(userForm.getCompany())) {
            String msg = "公司不能为空。";
            return CheckResult.failure(msg);
        }

        if (userForm.getEmerContact() != null && StringUtils.isEmpty(userForm.getEmerContact())) {
            String msg = "紧急联系人不能为空。";
            return CheckResult.failure(msg);
        }

        if (userForm.getEmerTel() != null && StringUtils.isEmpty(userForm.getEmerTel())) {
            String msg = "紧急联系人联系方式不能为空。";
            return CheckResult.failure(msg);
        }
        return CheckResult.success();
    }

    private CheckResult checkUpdateLogic(String username, UserForm userForm, String activeUsername) {
        if (!username.equals(activeUsername)) {
            UserDO activeUser = userDAO.get(activeUsername);
            RoleDO roleDO = roleDAO.get(activeUser.getRoleId());
            if (BooleanUtils.isNotTrue(roleDO.getManager())) {
                String msg = "禁止修改。";
                return CheckResult.failure(msg);
            }
        }

        UserDO userDO = userDAO.get(username);
        if (userDO == null) {
            String msg = "用户不存在。";
            return CheckResult.failure(msg);
        }

        if (userForm.getRoleId() != null && !userForm.getRoleId().equals(userDO.getRoleId())) {
            RoleDO roleDO = roleDAO.get(userForm.getRoleId());
            if (roleDO == null) {
                String msg = "角色不存在。";
                return CheckResult.failure(msg);
            }
        }

        return CheckResult.success();
    }

}
