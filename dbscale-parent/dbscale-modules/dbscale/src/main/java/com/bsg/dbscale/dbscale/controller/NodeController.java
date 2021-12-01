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

import com.bsg.dbscale.dbscale.annotation.UnderlineToCamel;
import com.bsg.dbscale.dbscale.query.NodeQuery;
import com.bsg.dbscale.service.service.NodeService;
import com.bsg.dbscale.service.service.Result;

@Controller
@RequestMapping(value = "nodes", headers = { "version=1.0" })
public class NodeController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private NodeService nodeService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Result list(@UnderlineToCamel NodeQuery nodeQuery, HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            com.bsg.dbscale.service.query.NodeQuery serviceQuery = new com.bsg.dbscale.service.query.NodeQuery();
            serviceQuery.setSiteId(nodeQuery.getSiteId());
            serviceQuery.setArch(nodeQuery.getArch());
            serviceQuery.setOs(nodeQuery.getOs());
            serviceQuery.setRole(nodeQuery.getRole());
            result = nodeService.list(serviceQuery);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("查询node信息异常：", e);
        }
        return result;
    }

}
