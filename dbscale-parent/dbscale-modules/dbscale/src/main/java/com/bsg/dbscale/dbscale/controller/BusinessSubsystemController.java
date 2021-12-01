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
import com.bsg.dbscale.dbscale.query.BusinessSubsystemQuery;
import com.bsg.dbscale.service.form.BusinessSubsystemForm;
import com.bsg.dbscale.service.service.BusinessSubsystemService;
import com.bsg.dbscale.service.service.Result;

@Controller
@RequestMapping(value = "business_subsystems", headers = { "version=1.0" })
@OperateLog(objType = "业务子系统")
public class BusinessSubsystemController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private BusinessSubsystemService businessSubsystemService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Result list(@UnderlineToCamel BusinessSubsystemQuery businessSubsystemQuery, HttpServletResponse response,
            HttpSession session) {
        Result result = null;
        try {
            UserDO userDO = businessSubsystemService.getActiveUser(session);
            String activeUsername = userDO.getUsername();

            com.bsg.dbscale.service.query.BusinessSubsystemQuery serviceQuery = new com.bsg.dbscale.service.query.BusinessSubsystemQuery();
            serviceQuery.setBusinessSystemId(businessSubsystemQuery.getBusinessSystemId());
            serviceQuery.setEnabled(businessSubsystemQuery.getEnabled());
            serviceQuery.setOwner(activeUsername);
            result = businessSubsystemService.list(serviceQuery);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("查询业务子系统信息异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{business_subsystem_id}", method = RequestMethod.GET)
    @ResponseBody
    public Result get(@PathVariable("business_subsystem_id") String businessSubsystemId, HttpServletResponse response) {
        Result result = null;
        try {
            result = businessSubsystemService.get(businessSubsystemId);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("查询业务子系统详情异常：", e);
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @OperateLog(action = "新增")
    public Result save(@RequestBody BusinessSubsystemForm businessSubsystemForm, HttpServletResponse response,
            HttpSession session) {
        Result result = null;
        try {
            UserDO userDO = businessSubsystemService.getActiveUser(session);
            String activeUsername = userDO.getUsername();

            result = businessSubsystemService.save(businessSubsystemForm, activeUsername);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_CREATED);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("新增业务子系统异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{business_subsystem_id}", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(action = "编辑")
    public Result update(@PathVariable("business_subsystem_id") String businessSubsystemId,
            @RequestBody BusinessSubsystemForm businessSubsystemForm, HttpServletResponse response) {
        Result result = null;
        try {
            result = businessSubsystemService.update(businessSubsystemId, businessSubsystemForm);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("编辑业务子系统异常：", e);
        }
        return result;
    }
    
    @RequestMapping(value = "/{business_subsystem_id}/enabled", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(action = "启用")
    public Result enabled(@PathVariable("business_subsystem_id") String businessSubsystemId, HttpServletResponse response) {
        Result result = null;
        try {
            result = businessSubsystemService.enabled(businessSubsystemId, true);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("启用业务子系统异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{business_subsystem_id}/disabled", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(action = "停用")
    public Result disabled(@PathVariable("business_subsystem_id") String businessSubsystemId, HttpServletResponse response) {
        Result result = null;
        try {
            result = businessSubsystemService.enabled(businessSubsystemId, false);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("停用业务子系统异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{business_subsystem_id}", method = RequestMethod.DELETE)
    @ResponseBody
    @OperateLog(action = "删除")
    public Result remove(@PathVariable("business_subsystem_id") String businessSubsystemId,
            HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            result = businessSubsystemService.remove(businessSubsystemId);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_NO_CONTENT);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("删除业务子系统异常：", e);
        }
        return result;
    }
}
