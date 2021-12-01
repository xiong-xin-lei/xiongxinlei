package com.bsg.dbscale.dbscale.controller;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bsg.dbscale.dao.domain.UserDO;
import com.bsg.dbscale.dbscale.annotation.OperateLog;
import com.bsg.dbscale.dbscale.annotation.UnderlineToCamel;
import com.bsg.dbscale.dbscale.query.UserQuery;
import com.bsg.dbscale.service.form.CheckForm;
import com.bsg.dbscale.service.form.PwdForm;
import com.bsg.dbscale.service.form.UserForm;
import com.bsg.dbscale.service.service.Result;
import com.bsg.dbscale.service.service.UserService;
import com.bsg.dbscale.service.util.BeanCopierUtil;

@Controller
@RequestMapping(value = "users", headers = { "version=1.0" })
@OperateLog(objType = "用户")
public class UserController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Result list(@UnderlineToCamel UserQuery userQuery, HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            UserDO userDO = userService.getActiveUser(session);
            String activeUsername = userDO.getUsername();

            com.bsg.dbscale.service.query.UserQuery serviceQuery = BeanCopierUtil.create(userQuery,
                    com.bsg.dbscale.service.query.UserQuery.class);
            result = userService.list(serviceQuery, activeUsername);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("查询用户信息异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{username}", method = RequestMethod.GET)
    @ResponseBody
    public Result get(@PathVariable("username") String username, HttpServletResponse response) {
        Result result = null;
        try {
            result = userService.get(username);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("查询用户详情异常：", e);
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @OperateLog(action = "新增")
    public Result save(@RequestBody UserForm userForm, HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            UserDO userDO = userService.getActiveUser(session);
            String activeUsername = userDO.getUsername();

            result = userService.save(userForm, activeUsername);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_CREATED);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("新增用户异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{username}", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(action = "编辑")
    public Result update(@PathVariable("username") String username, @RequestBody UserForm userForm,
            HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            UserDO userDO = userService.getActiveUser(session);
            String activeUsername = userDO.getUsername();

            result = userService.update(username, userForm, activeUsername);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("编辑用户异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{username}/enabled", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(action = "启用")
    public Result enable(@PathVariable("username") String username, HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            UserDO userDO = userService.getActiveUser(session);
            String activeUsername = userDO.getUsername();

            result = userService.enabled(username, true, activeUsername);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("启用用户异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{username}/disabled", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(action = "停用")
    public Result disable(@PathVariable("username") String username, HttpServletResponse response,
            HttpSession session) {
        Result result = null;
        try {
            UserDO userDO = userService.getActiveUser(session);
            String activeUsername = userDO.getUsername();

            result = userService.enabled(username, false, activeUsername);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("停用用户异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{username}/pwd", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(action = "修改密码")
    public Result updatePwd(@PathVariable("username") String username, @RequestBody PwdForm pwdForm,
            HttpServletResponse response) {
        Result result = null;
        try {
            result = userService.updatePwd(username, pwdForm);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("修改密码异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "check", method = RequestMethod.PUT)
    @ResponseBody
    public Result check(@RequestBody CheckForm checkForm, HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            UserDO userDO = userService.getActiveUser(session);
            String activeUsername = userDO.getUsername();

            result = userService.check(activeUsername, checkForm);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("检查用户异常：", e);
        }
        return result;
    }

}
