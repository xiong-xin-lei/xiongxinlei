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
import com.bsg.dbscale.service.form.SiteForm;
import com.bsg.dbscale.service.service.Result;
import com.bsg.dbscale.service.service.SiteService;

@Controller
@RequestMapping(value = "sites", headers = { "version=1.0" })
@OperateLog(objType = "站点")
public class SiteController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SiteService siteService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Result list(HttpServletResponse response) {
        Result result = null;
        try {
            result = siteService.list();
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("查询站点信息异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{site_id}", method = RequestMethod.GET)
    @ResponseBody
    public Result get(@PathVariable("site_id") String siteId, HttpServletResponse response) {
        Result result = null;
        try {
            result = siteService.get(siteId);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("查询站点详情异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{site_id}/resources", method = RequestMethod.GET)
    @ResponseBody
    public Result getResourceStatistics(@PathVariable("site_id") String siteId, HttpServletResponse response) {
        Result result = null;
        try {
            result = siteService.getResourceStatistics(siteId);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("查询站点资源统计异常：", e);
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @OperateLog(action = "新增")
    public Result save(@RequestBody SiteForm siteForm, HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            result = siteService.save(siteForm);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_CREATED);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("新增站点异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{site_id}", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(action = "编辑")
    public Result update(@PathVariable("site_id") String siteId, @RequestBody SiteForm siteForm,
            HttpServletResponse response) {
        Result result = null;
        try {
            result = siteService.update(siteId, siteForm);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("编辑站点异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{site_id}", method = RequestMethod.DELETE)
    @ResponseBody
    @OperateLog(action = "删除")
    public Result remove(@PathVariable("site_id") String siteId, HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            result = siteService.remove(siteId);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_NO_CONTENT);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("删除站点异常：", e);
        }
        return result;
    }
}
