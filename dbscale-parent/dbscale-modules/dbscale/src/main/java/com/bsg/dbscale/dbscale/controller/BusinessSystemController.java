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
import com.bsg.dbscale.service.form.BusinessSystemForm;
import com.bsg.dbscale.service.query.BusinessSystemQuery;
import com.bsg.dbscale.service.service.BusinessSystemService;
import com.bsg.dbscale.service.service.Result;

@Controller
@RequestMapping(value = "business_systems", headers = { "version=1.0" })
@OperateLog(objType = "业务系统")
public class BusinessSystemController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private BusinessSystemService businessSystemService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Result list(@RequestParam(name = "enabled", required = false) Boolean enabled, HttpServletResponse response,
            HttpSession session) {
        Result result = null;
        try {
            UserDO userDO = businessSystemService.getActiveUser(session);
            String activeUsername = userDO.getUsername();

            BusinessSystemQuery serviceQuery = new BusinessSystemQuery();
            serviceQuery.setOwner(activeUsername);
            serviceQuery.setEnabled(enabled);
            result = businessSystemService.list(serviceQuery);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("查询业务系统信息异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{business_system_id}", method = RequestMethod.GET)
    @ResponseBody
    public Result get(@PathVariable("business_system_id") String businessSystemId, HttpServletResponse response) {
        Result result = null;
        try {
            result = businessSystemService.get(businessSystemId);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("查询业务系统详情异常：", e);
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @OperateLog(action = "新增")
    public Result save(@RequestBody BusinessSystemForm businessSystemForm, HttpServletResponse response,
            HttpSession session) {
        Result result = null;
        try {
            UserDO userDO = businessSystemService.getActiveUser(session);
            String activeUsername = userDO.getUsername();

            result = businessSystemService.save(businessSystemForm, activeUsername);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_CREATED);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("新增业务系统异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{business_system_id}", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(action = "编辑")
    public Result update(@PathVariable("business_system_id") String businessSystemId,
            @RequestBody BusinessSystemForm businessSystemForm, HttpServletResponse response) {
        Result result = null;
        try {
            result = businessSystemService.update(businessSystemId, businessSystemForm);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("编辑业务系统异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{business_system_id}/enabled", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(action = "启用")
    public Result enabled(@PathVariable("business_system_id") String businessSystemId, HttpServletResponse response) {
        Result result = null;
        try {
            result = businessSystemService.enabled(businessSystemId, true);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("启用业务系统异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{business_system_id}/disabled", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(action = "停用")
    public Result disabled(@PathVariable("business_system_id") String businessSystemId, HttpServletResponse response) {
        Result result = null;
        try {
            result = businessSystemService.enabled(businessSystemId, false);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("停用业务系统异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{business_system_id}", method = RequestMethod.DELETE)
    @ResponseBody
    @OperateLog(action = "删除")
    public Result remove(@PathVariable("business_system_id") String businessSystemId, HttpServletResponse response,
            HttpSession session) {
        Result result = null;
        try {
            result = businessSystemService.remove(businessSystemId);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_NO_CONTENT);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("删除业务系统异常：", e);
        }
        return result;
    }
}
