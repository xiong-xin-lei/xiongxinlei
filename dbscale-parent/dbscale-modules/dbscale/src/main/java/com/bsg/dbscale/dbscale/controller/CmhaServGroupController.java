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
import com.bsg.dbscale.service.form.BackupForm;
import com.bsg.dbscale.service.form.CmhaServGroupUserForm;
import com.bsg.dbscale.service.form.DBSchemaForm;
import com.bsg.dbscale.service.form.PwdForm;
import com.bsg.dbscale.service.service.CmhaServGroupService;
import com.bsg.dbscale.service.service.Result;

@Controller
@RequestMapping(value = "serv_groups", headers = { "version=1.0" })
public class CmhaServGroupController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private CmhaServGroupService servGroupService;

    @RequestMapping(value = "/cmha", method = RequestMethod.GET)
    @ResponseBody
    public Result list(@UnderlineToCamel ServGroupQuery servGroupQuery, HttpServletResponse response,
            HttpSession session) {
        Result result = null;
        try {
            UserDO userDO = servGroupService.getActiveUser(session);
            String activeUsername = userDO.getUsername();

            com.bsg.dbscale.service.query.ServGroupQuery serviceQuery = new com.bsg.dbscale.service.query.ServGroupQuery();
            serviceQuery.setSiteId(servGroupQuery.getSiteId());
            serviceQuery.setCreateSuccess(servGroupQuery.getCreateSuccess());
            serviceQuery.setState(servGroupQuery.getState());
            result = servGroupService.list(serviceQuery, activeUsername);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("查询CMHA服务组信息异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/cmha/{serv_group_id}", method = RequestMethod.GET)
    @ResponseBody
    public Result get(@PathVariable("serv_group_id") String servGroupId,
            @RequestParam(name = "replication", defaultValue = "true") boolean replication,
            @RequestParam(name = "topology", defaultValue = "true") boolean topology, HttpServletResponse response,
            HttpSession session) {
        Result result = null;
        try {
            result = servGroupService.get(servGroupId, replication, topology);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("查询CMHA服务组详情异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/cmha/{serv_group_id}/backup", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(objType = "CMHA服务", action = "备份")
    public Result backup(@PathVariable("serv_group_id") String servGroupId, @RequestBody BackupForm backupForm,
            HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            UserDO userDO = servGroupService.getActiveUser(session);
            String activeUsername = userDO.getUsername();

            result = servGroupService.backup(servGroupId, backupForm, activeUsername);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("CMHA服务组备份异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/cmha/{serv_group_id}/db/schemas", method = RequestMethod.GET)
    @ResponseBody
    public Result listDBSchema(@PathVariable("serv_group_id") String servGroupId, HttpServletResponse response,
            HttpSession session) {
        Result result = null;
        try {
            result = servGroupService.listDBSchema(servGroupId);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("查询CMHA服务组-库信息异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/cmha/{serv_group_id}/db/schemas/{schema_name}", method = RequestMethod.GET)
    @ResponseBody
    public Result getDBSchema(@PathVariable("serv_group_id") String servGroupId,
            @PathVariable("schema_name") String dbSchemaName, HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            result = servGroupService.getDBSchema(servGroupId, dbSchemaName);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("查询CMHA服务组-库详情异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/cmha/{serv_group_id}/db/schemas", method = RequestMethod.POST)
    @ResponseBody
    @OperateLog(objType = "CMHA服务组-库", action = "新增")
    public Result saveDBSchema(@PathVariable("serv_group_id") String servGroupId,
            @RequestBody DBSchemaForm dbSchemaForm, HttpServletResponse response) {
        Result result = null;
        try {
            result = servGroupService.saveDBSchema(servGroupId, dbSchemaForm);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_CREATED);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("新增CMHA服务组-库异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/cmha/{serv_group_id}/db/schemas/{schema_name}", method = RequestMethod.DELETE)
    @ResponseBody
    @OperateLog(objType = "CMHA服务组-库", action = "删除")
    public Result removeDBSchema(@PathVariable("serv_group_id") String servGroupId,
            @PathVariable("schema_name") String dbSchemaName, HttpServletResponse response) {
        Result result = null;
        try {
            result = servGroupService.removeDBSchema(servGroupId, dbSchemaName);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_NO_CONTENT);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("删除CMHA服务组-库异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/cmha/{serv_group_id}/users", method = RequestMethod.GET)
    @ResponseBody
    public Result listUser(@PathVariable("serv_group_id") String servGroupId, HttpServletResponse response,
            HttpSession session) {
        Result result = null;
        try {
            result = servGroupService.listUser(servGroupId);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("查询CMHA服务组-用户信息异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/cmha/{serv_group_id}/users/{username}", method = RequestMethod.GET)
    @ResponseBody
    public Result getUser(@PathVariable("serv_group_id") String servGroupId,
            @PathVariable("username") String username, @RequestParam(value = "ip", required = false) String whiteIp,
            HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            result = servGroupService.getUser(servGroupId, username, whiteIp);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("查询CMHA服务组-用户详情异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/cmha/{serv_group_id}/users", method = RequestMethod.POST)
    @ResponseBody
    @OperateLog(objType = "CMHA服务组-用户", action = "新增")
    public Result saveUser(@PathVariable("serv_group_id") String servGroupId,
            @RequestBody CmhaServGroupUserForm userForm, HttpServletResponse response) {
        Result result = null;
        try {
            result = servGroupService.saveUser(servGroupId, userForm);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_CREATED);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("新增CMHA服务组-用户异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/cmha/{serv_group_id}/users/{username}", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(objType = "CMHA服务组-用户", action = "编辑")
    public Result updateUser(@PathVariable("serv_group_id") String servGroupId,
            @PathVariable("username") String username, @RequestParam(value = "ip", required = false) String whiteIp,
            @RequestBody CmhaServGroupUserForm userForm, HttpServletResponse response) {
        Result result = null;
        try {
            result = servGroupService.updateUser(servGroupId, username, whiteIp, userForm);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("编辑CMHA服务组-用户异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/cmha/{serv_group_id}/users/{username}/pwd/reset", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(objType = "CMHA服务组-用户", action = "重置密码")
    public Result resetUserPwd(@PathVariable("serv_group_id") String servGroupId,
            @PathVariable("username") String username, @RequestParam(value = "ip", required = false) String whiteIp,
            @RequestBody PwdForm pwdForm, HttpServletResponse response) {
        Result result = null;
        try {
            result = servGroupService.resetUserPwd(servGroupId, username, whiteIp, pwdForm);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("编辑CMHA服务组-用户异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/cmha/{serv_group_id}/users/{username}", method = RequestMethod.DELETE)
    @ResponseBody
    @OperateLog(objType = "CMHA服务组-用户", action = "删除")
    public Result removeUser(@PathVariable("serv_group_id") String servGroupId,
            @PathVariable("username") String username, @RequestParam(value = "ip", required = false) String whiteIp,
            HttpServletResponse response) {
        Result result = null;
        try {
            result = servGroupService.removeUser(servGroupId, username, whiteIp);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_NO_CONTENT);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("删除CMHA服务组-用户异常：", e);
        }
        return result;
    }
}
