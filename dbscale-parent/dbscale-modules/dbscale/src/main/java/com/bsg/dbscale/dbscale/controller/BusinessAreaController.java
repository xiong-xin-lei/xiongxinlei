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
import com.bsg.dbscale.dbscale.query.BusinessAreaQuery;
import com.bsg.dbscale.service.form.BusinessAreaForm;
import com.bsg.dbscale.service.service.BusinessAreaService;
import com.bsg.dbscale.service.service.Result;

@Controller
@RequestMapping(value = "business_areas", headers = { "version=1.0" })
@OperateLog(objType = "业务区")
public class BusinessAreaController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private BusinessAreaService businessAreaService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Result list(@UnderlineToCamel BusinessAreaQuery businessAreaQuery, HttpServletResponse response,
            HttpSession session) {
        Result result = null;
        try {
            com.bsg.dbscale.service.query.BusinessAreaQuery serviceQuery = new com.bsg.dbscale.service.query.BusinessAreaQuery();
            serviceQuery.setSiteId(businessAreaQuery.getSiteId());
            serviceQuery.setEnabled(businessAreaQuery.getEnabled());
            result = businessAreaService.list(serviceQuery);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("查询业务区信息异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{business_area_id}", method = RequestMethod.GET)
    @ResponseBody
    public Result get(@PathVariable("business_area_id") String businessAreaId, HttpServletResponse response) {
        Result result = null;
        try {
            result = businessAreaService.get(businessAreaId);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("查询业务区详情异常：", e);
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @OperateLog(action = "新增")
    public Result save(@RequestBody BusinessAreaForm businessAreaForm, HttpServletResponse response,
            HttpSession session) {
        Result result = null;
        try {
            UserDO userDO = businessAreaService.getActiveUser(session);
            String activeUsername = userDO.getUsername();

            result = businessAreaService.save(businessAreaForm, activeUsername);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_CREATED);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("新增业务区异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{business_area_id}", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(action = "编辑")
    public Result update(@PathVariable("business_area_id") String businessAreaId,
            @RequestBody BusinessAreaForm businessAreaForm, HttpServletResponse response) {
        Result result = null;
        try {
            result = businessAreaService.update(businessAreaId, businessAreaForm);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("编辑业务区异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{business_area_id}/enabled", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(action = "启用")
    public Result enabled(@PathVariable("business_area_id") String businessAreaId, HttpServletResponse response) {
        Result result = null;
        try {
            result = businessAreaService.enabled(businessAreaId, true);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("启用业务区异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{business_area_id}/disabled", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(action = "停用")
    public Result disabled(@PathVariable("business_area_id") String businessAreaId, HttpServletResponse response) {
        Result result = null;
        try {
            result = businessAreaService.enabled(businessAreaId, false);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("停用业务区异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{business_area_id}", method = RequestMethod.DELETE)
    @ResponseBody
    @OperateLog(action = "删除")
    public Result remove(@PathVariable("business_area_id") String businessAreaId, HttpServletResponse response,
            HttpSession session) {
        Result result = null;
        try {
            result = businessAreaService.remove(businessAreaId);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_NO_CONTENT);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("删除业务区异常：", e);
        }
        return result;
    }
}
