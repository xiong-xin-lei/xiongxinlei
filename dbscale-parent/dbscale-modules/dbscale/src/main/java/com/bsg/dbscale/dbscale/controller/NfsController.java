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
import com.bsg.dbscale.dbscale.annotation.UnderlineToCamel;
import com.bsg.dbscale.dbscale.query.NfsQuery;
import com.bsg.dbscale.service.form.NfsForm;
import com.bsg.dbscale.service.service.NfsService;
import com.bsg.dbscale.service.service.Result;

@Controller
@RequestMapping(value = "nfs", headers = { "version=1.0" })
@OperateLog(objType = "NFS")
public class NfsController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private NfsService nfsService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Result list(@UnderlineToCamel NfsQuery nfsQuery, HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            com.bsg.dbscale.service.query.NfsQuery serviceQuery = new com.bsg.dbscale.service.query.NfsQuery();
            serviceQuery.setSiteId(nfsQuery.getSiteId());
            serviceQuery.setBusinessAreaId(nfsQuery.getBusinessAreaId());
            serviceQuery.setEnabled(nfsQuery.getEnabled());
            result = nfsService.list(serviceQuery);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("查询NFS信息异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{nfs_id}", method = RequestMethod.GET)
    @ResponseBody
    public Result get(@PathVariable("nfs_id") String nfsId, HttpServletResponse response) {
        Result result = null;
        try {
            result = nfsService.get(nfsId);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("查询NFS详情异常：", e);
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @OperateLog(action = "新增")
    public Result save(@RequestBody NfsForm nfsForm, HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            result = nfsService.save(nfsForm);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_CREATED);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("新增NFS异常：", e);
        }
        return result;
    }
    
    @RequestMapping(value = "/{nfs_id}/enabled", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(action = "启用")
    public Result enabled(@PathVariable("nfs_id") String nfsId, HttpServletResponse response,
            HttpSession session) {
        Result result = null;
        try {
            result = nfsService.enabled(nfsId, true);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("启用NFS异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{nfs_id}/disabled", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(action = "停用")
    public Result disabled(@PathVariable("nfs_id") String nfsId, HttpServletResponse response,
            HttpSession session) {
        Result result = null;
        try {
            result = nfsService.enabled(nfsId, false);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("停用NFS异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{nfs_id}", method = RequestMethod.DELETE)
    @ResponseBody
    @OperateLog(action = "删除")
    public Result remove(@PathVariable("nfs_id") String nfsId, HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            result = nfsService.remove(nfsId);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_NO_CONTENT);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("删除NFS异常：", e);
        }
        return result;
    }
}
