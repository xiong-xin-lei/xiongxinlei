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
import com.bsg.dbscale.dbscale.query.BackupStrategyQuery;
import com.bsg.dbscale.service.form.BackupStrategyForm;
import com.bsg.dbscale.service.service.BackupStrategyService;
import com.bsg.dbscale.service.service.Result;

@Controller
@RequestMapping(value = "backup_strategies", headers = { "version=1.0" })
@OperateLog(objType = "备份策略")
public class BackupStrategyController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private BackupStrategyService backupStrategyService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Result list(@UnderlineToCamel BackupStrategyQuery backupStrategyQuery, HttpServletResponse response,
            HttpSession session) {
        Result result = null;
        try {
            com.bsg.dbscale.service.query.BackupStrategyQuery serviceQuery = new com.bsg.dbscale.service.query.BackupStrategyQuery();
            serviceQuery.setSiteId(backupStrategyQuery.getSiteId());
            serviceQuery.setServGroupId(backupStrategyQuery.getServGroupId());
            result = backupStrategyService.list(serviceQuery);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("查询备份策略信息异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{backup_strategy_id}", method = RequestMethod.GET)
    @ResponseBody
    public Result get(@PathVariable("backup_strategy_id") String backupStrategyId, HttpServletResponse response) {
        Result result = null;
        try {
            result = backupStrategyService.get(backupStrategyId);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("查询备份策略详情异常：", e);
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @OperateLog(action = "新增")
    public Result save(@RequestBody BackupStrategyForm backupStrategyForm, HttpServletResponse response,
            HttpSession session) {
        Result result = null;
        try {
            UserDO userDO = backupStrategyService.getActiveUser(session);
            String activeUsername = userDO.getUsername();

            result = backupStrategyService.save(backupStrategyForm, activeUsername);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_CREATED);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("新增备份策略异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{backup_strategy_id}", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(action = "编辑")
    public Result update(@PathVariable("backup_strategy_id") String backupStrategyId,
            @RequestBody BackupStrategyForm backupStrategyForm, HttpServletResponse response) {
        Result result = null;
        try {
            result = backupStrategyService.update(backupStrategyId, backupStrategyForm);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("编辑备份策略异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{backup_strategy_id}/enabled", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(action = "启用")
    public Result enabled(@PathVariable("backup_strategy_id") String backupStrategyId, HttpServletResponse response) {
        Result result = null;
        try {
            result = backupStrategyService.enabled(backupStrategyId, true);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("启用备份策略异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{backup_strategy_id}/disabled", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(action = "停用")
    public Result disabled(@PathVariable("backup_strategy_id") String backupStrategyId, HttpServletResponse response) {
        Result result = null;
        try {
            result = backupStrategyService.enabled(backupStrategyId, false);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("停用备份策略异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{backup_strategy_id}", method = RequestMethod.DELETE)
    @ResponseBody
    @OperateLog(action = "删除")
    public Result remove(@PathVariable("backup_strategy_id") String backupStrategyId, HttpServletResponse response,
            HttpSession session) {
        Result result = null;
        try {
            result = backupStrategyService.remove(backupStrategyId);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_NO_CONTENT);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("删除备份策略异常：", e);
        }
        return result;
    }
}
