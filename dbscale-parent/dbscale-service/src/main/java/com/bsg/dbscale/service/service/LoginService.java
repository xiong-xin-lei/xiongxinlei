package com.bsg.dbscale.service.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bsg.dbscale.dao.domain.UserDO;
import com.bsg.dbscale.service.check.CheckResult;
import com.bsg.dbscale.service.check.LoginCheck;
import com.bsg.dbscale.service.form.LoginForm;

/**
 * 登录登出逻辑处理类
 * 
 * @author HCK
 *
 */
@Service
public class LoginService extends BaseService {

    @Autowired
    private LoginCheck loginCheck;

    /**
     * 登录
     * 
     * @param request
     *            HttpServletRequest
     * @param loginForm
     *            LoginForm 登录表单数据
     * @return Result
     */
    public Result login(HttpServletRequest request, LoginForm loginForm) {
        CheckResult checkResult = loginCheck.checkSave(loginForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        UserDO userDO = userDAO.get(loginForm.getUsername());
        HttpSession session = request.getSession();
        session.setAttribute("user", userDO);

        return Result.success();
    }

    /**
     * 登出
     * 
     * @param request
     *            HttpServletRequest
     */
    public Result logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return Result.success();
    }

}
