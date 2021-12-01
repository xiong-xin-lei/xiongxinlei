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
import com.bsg.dbscale.dbscale.query.OrderGroupQuery;
import com.bsg.dbscale.service.form.ExamineForm;
import com.bsg.dbscale.service.form.OrderGroupForm;
import com.bsg.dbscale.service.service.OrderGroupService;
import com.bsg.dbscale.service.service.Result;

@Controller
@RequestMapping(value = "order_groups", headers = { "version=1.0" })
@OperateLog(objType = "工单")
public class OrderGroupController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private OrderGroupService orderGroupService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Result list(@UnderlineToCamel OrderGroupQuery orderGroupQuery, HttpServletResponse response,
            HttpSession session) {
        Result result = null;
        try {
            UserDO userDO = orderGroupService.getActiveUser(session);
            String activeUsername = userDO.getUsername();

            com.bsg.dbscale.service.query.OrderGroupQuery serviceQuery = new com.bsg.dbscale.service.query.OrderGroupQuery();
            serviceQuery.setSiteId(orderGroupQuery.getSiteId());
            serviceQuery.setCategory(orderGroupQuery.getCategory());
            serviceQuery.setCreateType(orderGroupQuery.getCreateType());
            serviceQuery.setState(orderGroupQuery.getState());
            result = orderGroupService.list(serviceQuery, activeUsername);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("查询工单组信息异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{order_group_id}", method = RequestMethod.GET)
    @ResponseBody
    public Result get(@PathVariable("order_group_id") String orderGroupId, HttpServletResponse response) {
        Result result = null;
        try {
            result = orderGroupService.get(orderGroupId);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("查询工单组详情异常：", e);
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @OperateLog(action = "申请")
    public Result save(@RequestBody OrderGroupForm orderGroupForm, HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            UserDO userDO = orderGroupService.getActiveUser(session);
            String activeUsername = userDO.getUsername();

            result = orderGroupService.save(orderGroupForm, activeUsername);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_CREATED);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("新增工单组异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{order_group_id}", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(action = "编辑")
    public Result update(@PathVariable("order_group_id") String orderGroupId,
            @RequestBody OrderGroupForm orderGroupForm, HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            UserDO userDO = orderGroupService.getActiveUser(session);
            String activeUsername = userDO.getUsername();

            result = orderGroupService.update(orderGroupId, orderGroupForm, activeUsername);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("编辑工单组异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{order_group_id}/examine", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(action = "审批")
    public Result examine(@PathVariable("order_group_id") String orderGroupId, @RequestBody ExamineForm examineForm,
            HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            UserDO userDO = orderGroupService.getActiveUser(session);
            String activeUsername = userDO.getUsername();

            result = orderGroupService.examine(orderGroupId, examineForm, activeUsername);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("工单组审批异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{order_group_id}/execute", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(action = "执行")
    public Result execute(@PathVariable("order_group_id") String orderGroupId, HttpServletResponse response,
            HttpSession session) {
        Result result = null;
        try {
            UserDO userDO = orderGroupService.getActiveUser(session);
            String activeUsername = userDO.getUsername();

            result = orderGroupService.execute(orderGroupId, activeUsername);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("工单组执行异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{order_group_id}", method = RequestMethod.DELETE)
    @ResponseBody
    @OperateLog(action = "删除")
    public Result remove(@PathVariable("order_group_id") String orderGroupId, HttpServletResponse response,
            HttpSession session) {
        Result result = null;
        try {
            result = orderGroupService.remove(orderGroupId);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_NO_CONTENT);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("删除工单组异常：", e);
        }
        return result;
    }
}
