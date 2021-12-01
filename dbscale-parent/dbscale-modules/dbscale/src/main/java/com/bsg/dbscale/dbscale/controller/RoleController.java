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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bsg.dbscale.dao.domain.UserDO;
import com.bsg.dbscale.dbscale.annotation.OperateLog;
import com.bsg.dbscale.service.form.RoleForm;
import com.bsg.dbscale.service.service.Result;
import com.bsg.dbscale.service.service.RoleCfgAppService;
import com.bsg.dbscale.service.service.RoleService;

@Controller
@RequestMapping(value = "roles", headers = { "version=1.0" })
@OperateLog(objType = "角色")
public class RoleController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleCfgAppService cfgAppService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Result list(HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            UserDO userDO = roleService.getActiveUser(session);
            String activeUsername = userDO.getUsername();

            result = roleService.list(activeUsername);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            logger.error("list role exception:", e);
        }
        return result;
    }

    @RequestMapping(value = "/{role_id}", method = RequestMethod.GET)
    @ResponseBody
    public Result get(@PathVariable("role_id") String roleId, HttpServletResponse response) {
        Result result = null;
        try {
            result = roleService.get(roleId);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            logger.error("get role exception:", e);
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @OperateLog(action = "新增")
    public Result save(@RequestBody RoleForm roleForm, HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            UserDO userDO = roleService.getActiveUser(session);
            String activeUsername = userDO.getUsername();

            result = roleService.save(roleForm, activeUsername);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_CREATED);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            logger.error("save role exception:", e);
        }
        return result;
    }

    @RequestMapping(value = "/{role_id}", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(action = "编辑")
    public Result update(@PathVariable("role_id") String roleId, @RequestBody RoleForm roleForm,
            HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            UserDO userDO = roleService.getActiveUser(session);
            String activeUsername = userDO.getUsername();

            result = roleService.update(roleId, roleForm, activeUsername);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            logger.error("update role exception:", e);
        }
        return result;
    }

    @RequestMapping(value = "/{role_id}", method = RequestMethod.DELETE)
    @ResponseBody
    @OperateLog(action = "删除")
    public Result remove(@PathVariable("role_id") String roleId, HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            UserDO userDO = roleService.getActiveUser(session);
            String activeUsername = userDO.getUsername();

            result = roleService.remove(roleId, activeUsername);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_NO_CONTENT);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            logger.error("remove role exception:", e);
        }
        return result;
    }

    @RequestMapping(value = "/cfg/apps", method = RequestMethod.GET)
    @ResponseBody
    public Result listCfgApp(@RequestParam("site_id") String siteId, HttpServletResponse response) {
        Result result = null;
        try {
            result = cfgAppService.list(siteId);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            logger.error("list app config exception:", e);
        }
        return result;
    }

    @RequestMapping(value = "/{role_id}/cfg/apps", method = RequestMethod.GET)
    @ResponseBody
    public Result getCfgApp(@PathVariable("role_id") String roleId, @RequestParam("site_id") String siteId,
            HttpServletResponse response) {
        Result result = null;
        try {
            result = cfgAppService.get(roleId, siteId);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            logger.error("get role app config exception:", e);
        }
        return result;
    }

    @RequestMapping(value = "/{role_id}/cfg/apps", method = RequestMethod.POST)
    @ResponseBody
    @OperateLog(action = "功能配置")
    public Result saveCfgApp(@PathVariable("role_id") String roleId, @RequestBody List<Long> appIds,
            HttpServletResponse response) {
        Result result = null;
        try {
            result = cfgAppService.save(roleId, appIds);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_CREATED);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            logger.error("save role app config  exception:", e);
        }
        return result;
    }

}
