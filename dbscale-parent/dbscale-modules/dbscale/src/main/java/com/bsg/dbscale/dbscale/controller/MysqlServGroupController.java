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
import com.bsg.dbscale.service.form.DBSchemaForm;
import com.bsg.dbscale.service.form.MysqlServGroupUserForm;
import com.bsg.dbscale.service.form.PwdForm;
import com.bsg.dbscale.service.service.MysqlServGroupService;
import com.bsg.dbscale.service.service.Result;

@Controller
@RequestMapping(value = "serv_groups", headers = { "version=1.0" })
public class MysqlServGroupController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MysqlServGroupService mysqlServGroupService;

    @RequestMapping(value = "/mysql", method = RequestMethod.GET)
    @ResponseBody
    public Result list(@UnderlineToCamel ServGroupQuery servGroupQuery, HttpServletResponse response,
            HttpSession session) {
        Result result = null;
        try {
            UserDO userDO = mysqlServGroupService.getActiveUser(session);
            String activeUsername = userDO.getUsername();

            com.bsg.dbscale.service.query.ServGroupQuery serviceQuery = new com.bsg.dbscale.service.query.ServGroupQuery();
            serviceQuery.setSiteId(servGroupQuery.getSiteId());
            serviceQuery.setCreateSuccess(servGroupQuery.getCreateSuccess());
            serviceQuery.setState(servGroupQuery.getState());
            result = mysqlServGroupService.list(serviceQuery, activeUsername);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("查询MySQL服务组信息异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/mysql/{serv_group_id}", method = RequestMethod.GET)
    @ResponseBody
    public Result get(@PathVariable("serv_group_id") String servGroupId,
            @RequestParam(name = "replication", defaultValue = "true") boolean replication,
            @RequestParam(name = "topology", defaultValue = "true") boolean topology, HttpServletResponse response,
            HttpSession session) {
        Result result = null;
        try {
            result = mysqlServGroupService.get(servGroupId, replication);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("查询MySQL服务组详情异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/mysql/{serv_group_id}/backup", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(objType = "MySQL服务组", action = "备份")
    public Result backup(@PathVariable("serv_group_id") String servGroupId, @RequestBody BackupForm backupForm,
            HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            UserDO userDO = mysqlServGroupService.getActiveUser(session);
            String activeUsername = userDO.getUsername();

            result = mysqlServGroupService.backup(servGroupId, backupForm, activeUsername);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("MySQL服务组备份异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/mysql/{serv_group_id}/db/schemas", method = RequestMethod.GET)
    @ResponseBody
    public Result listDBSchema(@PathVariable("serv_group_id") String servGroupId, HttpServletResponse response,
            HttpSession session) {
        Result result = null;
        try {
            result = mysqlServGroupService.listDBSchema(servGroupId);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("查询MySQL服务组-库信息异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/mysql/{serv_group_id}/db/schemas/{schema_name}", method = RequestMethod.GET)
    @ResponseBody
    public Result getDBSchema(@PathVariable("serv_group_id") String servGroupId,
            @PathVariable("schema_name") String dbSchemaName, HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            result = mysqlServGroupService.getDBSchema(servGroupId, dbSchemaName);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("查询MySQL服务组-库详情异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/mysql/{serv_group_id}/db/schemas", method = RequestMethod.POST)
    @ResponseBody
    @OperateLog(objType = "MySQL服务组-库", action = "新增")
    public Result saveDBSchema(@PathVariable("serv_group_id") String servGroupId,
            @RequestBody DBSchemaForm dbSchemaForm, HttpServletResponse response) {
        Result result = null;
        try {
            result = mysqlServGroupService.saveDBSchema(servGroupId, dbSchemaForm);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_CREATED);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("新增MySQL服务组-库异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/mysql/{serv_group_id}/db/schemas/{schema_name}", method = RequestMethod.DELETE)
    @ResponseBody
    @OperateLog(objType = "MySQL服务组-库", action = "删除")
    public Result removeDBSchema(@PathVariable("serv_group_id") String servGroupId,
            @PathVariable("schema_name") String dbSchemaName, HttpServletResponse response) {
        Result result = null;
        try {
            result = mysqlServGroupService.removeDBSchema(servGroupId, dbSchemaName);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_NO_CONTENT);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("删除MySQL服务组-库异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/mysql/{serv_group_id}/users", method = RequestMethod.GET)
    @ResponseBody
    public Result listDBUser(@PathVariable("serv_group_id") String servGroupId, HttpServletResponse response,
            HttpSession session) {
        Result result = null;
        try {
            result = mysqlServGroupService.listUser(servGroupId);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("查询MySQL服务组-用户信息异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/mysql/{serv_group_id}/users/{username}", method = RequestMethod.GET)
    @ResponseBody
    public Result getDBUser(@PathVariable("serv_group_id") String servGroupId,
            @PathVariable("username") String username, @RequestParam(value = "ip", required = false) String whiteIp,
            HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            result = mysqlServGroupService.getUser(servGroupId, username, whiteIp);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("查询MySQL服务组-用户详情异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/mysql/{serv_group_id}/users", method = RequestMethod.POST)
    @ResponseBody
    @OperateLog(objType = "MySQL服务组-用户", action = "新增")
    public Result saveDBUser(@PathVariable("serv_group_id") String servGroupId,
            @RequestBody MysqlServGroupUserForm dbUserForm, HttpServletResponse response) {
        Result result = null;
        try {
            result = mysqlServGroupService.saveUser(servGroupId, dbUserForm);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_CREATED);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("新增MySQL服务组-用户异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/mysql/{serv_group_id}/users/{username}", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(objType = "MySQL服务组-用户", action = "编辑")
    public Result updateUser(@PathVariable("serv_group_id") String servGroupId,
            @PathVariable("username") String username, @RequestParam(value = "ip", required = false) String whiteIp,
            @RequestBody MysqlServGroupUserForm dbUserForm, HttpServletResponse response) {
        Result result = null;
        try {
            result = mysqlServGroupService.updateUser(servGroupId, username, whiteIp, dbUserForm);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("编辑MySQL服务组-用户异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/mysql/{serv_group_id}/users/{username}/pwd/reset", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(objType = "MySQL服务组-用户", action = "重置密码")
    public Result resetUserPwd(@PathVariable("serv_group_id") String servGroupId,
            @PathVariable("username") String username, @RequestParam(value = "ip", required = false) String whiteIp,
            @RequestBody PwdForm pwdForm, HttpServletResponse response) {
        Result result = null;
        try {
            result = mysqlServGroupService.resetUserPwd(servGroupId, username, whiteIp, pwdForm);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("编辑MySQL服务组-用户异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/mysql/{serv_group_id}/users/{username}", method = RequestMethod.DELETE)
    @ResponseBody
    @OperateLog(objType = "MySQL服务组-用户", action = "删除")
    public Result removeUser(@PathVariable("serv_group_id") String servGroupId,
            @PathVariable("username") String username, @RequestParam(value = "ip", required = false) String whiteIp,
            HttpServletResponse response) {
        Result result = null;
        try {
            result = mysqlServGroupService.removeUser(servGroupId, username, whiteIp);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_NO_CONTENT);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("删除MySQL服务组-用户异常：", e);
        }
        return result;
    }
}
