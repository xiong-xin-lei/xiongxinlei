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

import com.bsg.dbscale.dbscale.annotation.OperateLog;
import com.bsg.dbscale.service.form.SubtaskCfgForm;
import com.bsg.dbscale.service.service.Result;
import com.bsg.dbscale.service.service.SubtaskCfgService;

@Controller
@RequestMapping(value = "subtask/cfgs", headers = { "version=1.0" })
@OperateLog(objType = "任务配置")
public class SubtaskCfgController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SubtaskCfgService subtaskCfgService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Result list(HttpServletResponse response) {
        Result result = null;
        try {
            result = subtaskCfgService.list();
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("查询任务配置信息异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{obj_type}/{action_type}", method = RequestMethod.GET)
    @ResponseBody
    public Result list(@PathVariable("obj_type") String objType, @PathVariable("action_type") String actionType,
            HttpServletResponse response) {
        Result result = null;
        try {
            result = subtaskCfgService.get(objType, actionType);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("查询任务配置详情异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{obj_type}/{action_type}", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(action = "编辑")
    public Result update(@PathVariable("obj_type") String objType, @PathVariable("action_type") String actionType,
            @RequestBody SubtaskCfgForm subtaskCfgForm, HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            result = subtaskCfgService.update(objType, actionType, subtaskCfgForm);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("编辑任务配置异常：", e);
        }
        return result;
    }

}
