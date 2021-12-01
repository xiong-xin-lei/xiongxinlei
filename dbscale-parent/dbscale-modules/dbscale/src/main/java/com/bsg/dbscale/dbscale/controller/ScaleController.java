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

import com.bsg.dbscale.dbscale.annotation.OperateLog;
import com.bsg.dbscale.dbscale.annotation.UnderlineToCamel;
import com.bsg.dbscale.dbscale.query.ScaleQuery;
import com.bsg.dbscale.service.form.ScaleForm;
import com.bsg.dbscale.service.service.Result;
import com.bsg.dbscale.service.service.ScaleService;

@Controller
@RequestMapping(value = "scales", headers = { "version=1.0" })
@OperateLog(objType = "规模")
public class ScaleController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ScaleService scaleService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Result list(@UnderlineToCamel ScaleQuery scaleQuery, HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            com.bsg.dbscale.service.query.ScaleQuery serviceQuery = new com.bsg.dbscale.service.query.ScaleQuery();
            serviceQuery.setType(scaleQuery.getType());
            serviceQuery.setEnabled(scaleQuery.getEnabled());
            result = scaleService.list(serviceQuery);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("查询规模信息异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{type}", method = RequestMethod.GET)
    @ResponseBody
    public Result get(@PathVariable("type") String type, @RequestParam("cpu") Double cpuCnt,
            @RequestParam("mem") Double memSize, HttpServletResponse response) {
        Result result = null;
        try {
            result = scaleService.get(type, cpuCnt, memSize);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("查询规模详情异常：", e);
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @OperateLog(action = "新增")
    public Result save(@RequestBody ScaleForm scaleForm, HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            result = scaleService.save(scaleForm);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_CREATED);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("新增规模异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{type}/enabled", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(action = "启用")
    public Result enabled(@PathVariable("type") String type, @RequestParam("cpu") Double cpuCnt,
            @RequestParam("mem") Double memSize, HttpServletResponse response) {
        Result result = null;
        try {
            result = scaleService.enabled(type, cpuCnt, memSize, true);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("启用规模异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{type}/disabled", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(action = "停用")
    public Result disabled(@PathVariable("type") String type, @RequestParam("cpu") Double cpuCnt,
            @RequestParam("mem") Double memSize, HttpServletResponse response) {
        Result result = null;
        try {
            result = scaleService.enabled(type, cpuCnt, memSize, false);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("停用规模异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{type}", method = RequestMethod.DELETE)
    @ResponseBody
    @OperateLog(action = "删除")
    public Result remove(@PathVariable("type") String type, @RequestParam("cpu") Double cpuCnt,
            @RequestParam("mem") Double memSize, HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            result = scaleService.remove(type, cpuCnt, memSize);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_NO_CONTENT);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("删除规模异常：", e);
        }
        return result;
    }
}
