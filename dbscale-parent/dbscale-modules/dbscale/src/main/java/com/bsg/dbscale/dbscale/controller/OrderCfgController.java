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

import com.bsg.dbscale.dbscale.annotation.OperateLog;
import com.bsg.dbscale.service.form.OrderCfgForm;
import com.bsg.dbscale.service.service.OrderCfgService;
import com.bsg.dbscale.service.service.Result;

@Controller
@RequestMapping(value = "orders/cfgs", headers = { "version=1.0" })
@OperateLog(objType = "工单配置")
public class OrderCfgController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private OrderCfgService orderCfgService;

    @RequestMapping(value = "{category}", method = RequestMethod.GET)
    @ResponseBody
    public Result list(@PathVariable("category") String category, HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            result = orderCfgService.list(category);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("查询工单配置系统信息异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "{category}", method = RequestMethod.POST)
    @ResponseBody
    @OperateLog(action = "新增")
    public Result save(@PathVariable("category") String category, @RequestBody List<OrderCfgForm> orderCfgForms,
            HttpServletResponse response) {
        Result result = null;
        try {
            result = orderCfgService.save(category, orderCfgForms);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_CREATED);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("新增工单配置系统异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "{category}", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(action = "编辑")
    public Result update(@PathVariable("category") String category, @RequestBody List<OrderCfgForm> orderCfgForms,
            HttpServletResponse response) {
        Result result = null;
        try {
            result = orderCfgService.update(category, orderCfgForms);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("编辑工单配置系统异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "{category}", method = RequestMethod.DELETE)
    @ResponseBody
    @OperateLog(action = "删除")
    public Result remove(@PathVariable("category") String category, HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            result = orderCfgService.remove(category);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_NO_CONTENT);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("删除工单配置系统异常：", e);
        }
        return result;
    }
}
