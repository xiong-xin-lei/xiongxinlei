package com.bsg.dbscale.dbscale.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bsg.dbscale.dbscale.annotation.OperateLog;
import com.bsg.dbscale.service.form.LoginForm;
import com.bsg.dbscale.service.service.LoginService;
import com.bsg.dbscale.service.service.Result;

/**
 * 登录登出
 * 
 * @author HCK
 */
@Controller
@RequestMapping(headers = { "version=1.0" })
@OperateLog(objType = "用户")
public class LoginController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private LoginService loginService;

    /**
     * 登录
     * 
     * @param loginForm
     *            LoginForm 登录表单数据
     * @param request
     *            HttpServletRequest
     * @param response
     *            HttpServletResponse
     * @return Result
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    @OperateLog(action = "登录")
    public Result save(@RequestBody LoginForm loginForm, HttpServletRequest request, HttpServletResponse response) {
        Result result = null;
        try {
            result = loginService.login(request, loginForm);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_CREATED);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("登录异常:", e);
        }
        return result;
    }

    /**
     * 登出
     * 
     * @param request
     *            HttpServletRequest
     * @param response
     *            HttpServletResponse
     * @return Result
     */
    @RequestMapping(value = "logout", method = RequestMethod.PUT)
    @ResponseBody
    public Result logout(HttpServletRequest request, HttpServletResponse response) {
        Result result = null;
        try {
            result = loginService.logout(request);
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("登出异常:", e);
        }
        return result;
    }

}
