package com.bsg.dbscale.service.check;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.bsg.dbscale.dao.domain.UserDO;
import com.bsg.dbscale.service.form.LoginForm;

@Service
public class LoginCheck extends BaseCheck {

    public CheckResult checkSave(LoginForm loginForm) {
        if (StringUtils.isBlank(loginForm.getUsername())) {
            String msg = "用户名不能为空。";
            return CheckResult.failure(msg);
        }

        if (StringUtils.isBlank(loginForm.getPassword())) {
            String msg = "密码不能为空。";
            return CheckResult.failure(msg);
        }

        String password = DigestUtils.md5DigestAsHex(loginForm.getPassword().getBytes());
        UserDO userDO = userDAO.get(loginForm.getUsername());
        if (userDO == null) {
            String msg = "用户名不存在。";
            return CheckResult.failure(msg);
        }

        if (!userDO.getPassword().equals(password)) {
            String msg = "密码错误。";
            return CheckResult.failure(msg);
        }

        if (!userDO.getEnabled()) {
            String msg = "用户已停用。";
            return CheckResult.failure(msg);
        }

        return CheckResult.success();
    }

}
