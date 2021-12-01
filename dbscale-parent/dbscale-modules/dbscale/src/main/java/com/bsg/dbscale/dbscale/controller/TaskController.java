package com.bsg.dbscale.dbscale.controller;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bsg.dbscale.dbscale.annotation.UnderlineToCamel;
import com.bsg.dbscale.dbscale.query.TaskQuery;
import com.bsg.dbscale.service.service.Result;
import com.bsg.dbscale.service.service.TaskService;

@Controller
@RequestMapping(value = "tasks", headers = { "version=1.0" })
public class TaskController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TaskService taskService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Result list(@UnderlineToCamel TaskQuery taskQuery, HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            com.bsg.dbscale.service.query.TaskQuery serviceQuery = new com.bsg.dbscale.service.query.TaskQuery();
            serviceQuery.setSiteId(taskQuery.getSiteId());
            serviceQuery.setObjType(taskQuery.getObjType());
            serviceQuery.setObjId(taskQuery.getObjId());
            serviceQuery.setStart(taskQuery.getStart());
            serviceQuery.setEnd(taskQuery.getEnd());
            serviceQuery.setOwner(taskQuery.getOwner());
            result = taskService.list(serviceQuery);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("查询任务信息异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{task_id}", method = RequestMethod.GET)
    @ResponseBody
    public Result get(@PathVariable("task_id") String taskId, HttpServletResponse response) {
        Result result = null;
        try {
            result = taskService.get(taskId);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("查询任务详情异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{task_id}/cancel", method = RequestMethod.PUT)
    @ResponseBody
    public Result cancel(@PathVariable("task_id") String taskId, HttpServletResponse response) {
        Result result = null;
        try {
            result = taskService.cancel(taskId);
            if (result != null && result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("取消任务异常：", e);
        }
        return result;
    }
}
