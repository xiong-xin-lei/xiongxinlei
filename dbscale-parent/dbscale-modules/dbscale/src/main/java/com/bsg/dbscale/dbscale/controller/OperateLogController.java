package com.bsg.dbscale.dbscale.controller;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bsg.dbscale.dao.domain.UserDO;
import com.bsg.dbscale.dbscale.annotation.UnderlineToCamel;
import com.bsg.dbscale.dbscale.query.ForceRebuildLogQuery;
import com.bsg.dbscale.dbscale.query.OperateLogQuery;
import com.bsg.dbscale.service.service.ForceRebuildLogService;
import com.bsg.dbscale.service.service.OperateLogService;
import com.bsg.dbscale.service.service.Result;

@Controller
@RequestMapping(value = "operatelogs", headers = { "version=1.0" })
public class OperateLogController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private OperateLogService operateLogService;

    @Autowired
    private ForceRebuildLogService forceRebuildLogService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Result list(@UnderlineToCamel OperateLogQuery operateLogQuery, HttpServletResponse response,
            HttpSession session) {
        Result result = null;
        try {
            UserDO userDO = operateLogService.getActiveUser(session);
            String activeUsername = userDO.getUsername();

            com.bsg.dbscale.service.query.OperateLogQuery serviceQuery = new com.bsg.dbscale.service.query.OperateLogQuery();
            serviceQuery.setSiteId(operateLogQuery.getSiteId());
            serviceQuery.setStart(operateLogQuery.getStart());
            serviceQuery.setEnd(operateLogQuery.getEnd());
            result = operateLogService.list(serviceQuery, activeUsername);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("查询操作记录信息异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/force_rebuild", method = RequestMethod.GET)
    @ResponseBody
    public Result list(@UnderlineToCamel ForceRebuildLogQuery forceRebuildLogQuery, HttpServletResponse response,
            HttpSession session) {
        Result result = null;
        try {

            com.bsg.dbscale.service.query.ForceRebuildLogQuery serviceQuery = new com.bsg.dbscale.service.query.ForceRebuildLogQuery();
            serviceQuery.setSiteId(forceRebuildLogQuery.getSiteId());
            serviceQuery.setStart(forceRebuildLogQuery.getStart());
            serviceQuery.setEnd(forceRebuildLogQuery.getEnd());
            result = forceRebuildLogService.list(serviceQuery);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("查询强制重建记录信息异常：", e);
        }
        return result;
    }
}
