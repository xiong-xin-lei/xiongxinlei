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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bsg.dbscale.dao.domain.UserDO;
import com.bsg.dbscale.dbscale.annotation.OperateLog;
import com.bsg.dbscale.dbscale.annotation.UnderlineToCamel;
import com.bsg.dbscale.dbscale.query.ServGroupQuery;
import com.bsg.dbscale.service.form.ResetPwdForm;
import com.bsg.dbscale.service.service.RedisServGroupService;
import com.bsg.dbscale.service.service.Result;

@Controller
@RequestMapping(value = "serv_groups", headers = { "version=1.0" })
public class RedisServGroupController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RedisServGroupService redisServGroupService;

    @RequestMapping(value = "/redis", method = RequestMethod.GET)
    @ResponseBody
    public Result list(@UnderlineToCamel ServGroupQuery servGroupQuery, HttpServletResponse response,
            HttpSession session) {
        Result result = null;
        try {
            UserDO userDO = redisServGroupService.getActiveUser(session);
            String activeUsername = userDO.getUsername();

            com.bsg.dbscale.service.query.ServGroupQuery serviceQuery = new com.bsg.dbscale.service.query.ServGroupQuery();
            serviceQuery.setSiteId(servGroupQuery.getSiteId());
            serviceQuery.setCreateSuccess(servGroupQuery.getCreateSuccess());
            serviceQuery.setState(servGroupQuery.getState());
            result = redisServGroupService.list(serviceQuery, activeUsername);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("查询REDIS服务组信息异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/redis/{serv_group_id}", method = RequestMethod.GET)
    @ResponseBody
    public Result get(@PathVariable("serv_group_id") String servGroupId,
            @RequestParam(value = "type", required = false) String type, HttpServletResponse response,
            HttpSession session) {
        Result result = null;
        try {
            result = redisServGroupService.get(servGroupId, type);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("查询REDIS服务组详情异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/redis/{serv_group_id}/pwd", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(objType = "Redis服务组", action = "重置密码")
    public Result resetPwd(@PathVariable("serv_group_id") String servGroupId, @RequestBody ResetPwdForm resetPwdForm,
            HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            UserDO userDO = redisServGroupService.getActiveUser(session);
            String activeUsername = userDO.getUsername();

            result = redisServGroupService.resetPwd(servGroupId, resetPwdForm, activeUsername);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("Redis服务组重置密码异常：", e);
        }
        return result;
    }

}
