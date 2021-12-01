package com.bsg.dbscale.dbscale.controller;

import java.util.List;

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
import com.bsg.dbscale.dbscale.query.GroupQuery;
import com.bsg.dbscale.service.form.GroupForm;
import com.bsg.dbscale.service.service.GroupService;
import com.bsg.dbscale.service.service.Result;

@Controller
@RequestMapping(value = "groups", headers = { "version=1.0" })
@OperateLog(objType = "组别")
public class GroupController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private GroupService groupService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Result list(@UnderlineToCamel GroupQuery groupQuery, HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            UserDO userDO = groupService.getActiveUser(session);
            String activeUsername = userDO.getUsername();

            com.bsg.dbscale.service.query.GroupQuery serviceQuery = new com.bsg.dbscale.service.query.GroupQuery();
            serviceQuery.setName(groupQuery.getName());
            serviceQuery.setOwner(activeUsername);

            result = groupService.list(serviceQuery);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("查询组别信息异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{group_id}", method = RequestMethod.GET)
    @ResponseBody
    public Result get(@PathVariable("group_id") String groupId, HttpServletResponse response) {
        Result result = null;
        try {
            result = groupService.get(groupId);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("查询组别详情异常：", e);
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @OperateLog(action = "新增")
    public Result save(@RequestBody GroupForm groupForm, HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            UserDO userDO = groupService.getActiveUser(session);
            String activeUsername = userDO.getUsername();

            result = groupService.save(groupForm, activeUsername);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_CREATED);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("新增组别异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{group_id}", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(action = "编辑")
    public Result update(@PathVariable("group_id") String groupId, @RequestBody GroupForm groupForm,
            HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            UserDO userDO = groupService.getActiveUser(session);
            String activeUsername = userDO.getUsername();

            result = groupService.update(groupId, groupForm, activeUsername);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("编辑组别异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{group_id}", method = RequestMethod.DELETE)
    @ResponseBody
    @OperateLog(action = "删除")
    public Result remove(@PathVariable("group_id") String groupId, HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            UserDO userDO = groupService.getActiveUser(session);
            String activeUsername = userDO.getUsername();

            result = groupService.remove(groupId, activeUsername);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_NO_CONTENT);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("删除组别异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{group_id}/users", method = RequestMethod.POST)
    @ResponseBody
    @OperateLog(objType = "组别用户", action = "新增")
    public Result saveUser(@PathVariable("group_id") String groupId, @RequestBody List<String> usernames,
            HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            UserDO userDO = groupService.getActiveUser(session);
            String activeUsername = userDO.getUsername();

            result = groupService.saveUsers(groupId, usernames, activeUsername);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_CREATED);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("添加组用户异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{group_id}/users/{username}", method = RequestMethod.DELETE)
    @ResponseBody
    @OperateLog(objType = "组别用户", action = "删除")
    public Result removeUser(@PathVariable("group_id") String groupId, @PathVariable("username") String username,
            HttpServletResponse response) {
        Result result = null;
        try {
            result = groupService.removeUser(groupId, username);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_NO_CONTENT);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("删除组用户异常：", e);
        }
        return result;
    }
}
