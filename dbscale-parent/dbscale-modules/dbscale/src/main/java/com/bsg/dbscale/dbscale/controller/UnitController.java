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
import com.bsg.dbscale.service.form.BackupForm;
import com.bsg.dbscale.service.form.MaintenanceForm;
import com.bsg.dbscale.service.form.UnitRebuildForm;
import com.bsg.dbscale.service.form.UnitRebuildInitForm;
import com.bsg.dbscale.service.form.UnitRestoreForm;
import com.bsg.dbscale.service.service.Result;
import com.bsg.dbscale.service.service.UnitService;

@Controller
@RequestMapping(value = "units", headers = { "version=1.0" })
@OperateLog(objType = "单元")
public class UnitController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UnitService unitService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Result list(@RequestParam("site_id") String siteId,
            @RequestParam(value = "state", required = false) String state, HttpServletResponse response,
            HttpSession session) {
        Result result = null;
        try {
            result = unitService.list(siteId, state);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("查询单元信息异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{unit_id}/monitor", method = RequestMethod.GET)
    @ResponseBody
    public Result monitor(@PathVariable("unit_id") String unitId,
            @RequestParam(name = "type", required = false) String type, HttpServletResponse response) {
        Result result = null;
        try {
            result = unitService.monitor(unitId, type);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("单元监控异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{unit_id}/start", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(action = "启动")
    public Result start(@PathVariable("unit_id") String unitId, HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            UserDO userDO = unitService.getActiveUser(session);
            String activeUsername = userDO.getUsername();

            result = unitService.start(unitId, activeUsername);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("启动单元异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{unit_id}/stop", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(action = "停止")
    public Result stop(@PathVariable("unit_id") String unitId, HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            UserDO userDO = unitService.getActiveUser(session);
            String activeUsername = userDO.getUsername();

            result = unitService.stop(unitId, activeUsername);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("停止单元异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{unit_id}/rebuild", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(action = "重建")
    public Result rebuild(@PathVariable("unit_id") String unitId, @RequestBody UnitRebuildForm unitRebuildForm,
            HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            UserDO userDO = unitService.getActiveUser(session);
            String activeUsername = userDO.getUsername();

            result = unitService.rebuild(unitId, unitRebuildForm, activeUsername);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("重建单元异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{unit_id}/rebuild_init", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(action = "强制重建初始化")
    public Result rebuildInit(@PathVariable("unit_id") String unitId,
            @RequestBody(required = false) UnitRebuildInitForm unitRebuildInitForm, HttpServletResponse response,
            HttpSession session) {
        Result result = null;
        try {
            UserDO userDO = unitService.getActiveUser(session);
            String activeUsername = userDO.getUsername();

            result = unitService.rebuildInit(unitId, unitRebuildInitForm, activeUsername);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("强制重建初始化异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/mysql/{unit_id}/backup", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(action = "备份")
    public Result backup(@PathVariable("unit_id") String unitId, @RequestBody BackupForm backupForm,
            HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            UserDO userDO = unitService.getActiveUser(session);
            String activeUsername = userDO.getUsername();

            result = unitService.backup(unitId, backupForm, activeUsername);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("备份单元异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/mysql/{unit_id}/restore", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(action = "还原")
    public Result restore(@PathVariable("unit_id") String unitId, @RequestBody UnitRestoreForm unitRestoreForm,
            HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            UserDO userDO = unitService.getActiveUser(session);
            String activeUsername = userDO.getUsername();

            result = unitService.restore(unitId, unitRestoreForm, activeUsername);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("单元还原异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{unit_id}/terminals", method = RequestMethod.GET)
    @ResponseBody
    public Result getTerminal(@PathVariable("unit_id") String unitId, HttpServletResponse response,
            HttpSession session) {
        Result result = null;
        try {
            result = unitService.getTerminal(unitId);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("单元socket连接异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{unit_id}/events", method = RequestMethod.GET)
    @ResponseBody
    public Result getEvent(@PathVariable("unit_id") String unitId, HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            result = unitService.getEvent(unitId);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("单元事件异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{unit_id}/role/master", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(action = "设置主")
    public Result updateRole(@PathVariable("unit_id") String unitId, HttpServletResponse response,
            HttpSession session) {
        Result result = null;
        try {
            result = unitService.resetMaster(unitId);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("设置主异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/mysql/{unit_id}/maintenance", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(objType = "单元", action = "维护模式")
    public Result maintenanceForm(@PathVariable("unit_id") String unitId, @RequestBody MaintenanceForm maintenanceForm,
            HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            result = unitService.maintenance(unitId, maintenanceForm);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("维护式异常：", e);
        }
        return result;
    }
}
