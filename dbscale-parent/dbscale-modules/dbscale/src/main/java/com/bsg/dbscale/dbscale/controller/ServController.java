package com.bsg.dbscale.dbscale.controller;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bsg.dbscale.service.service.Result;
import com.bsg.dbscale.service.service.ServService;

@Controller
@RequestMapping(value = "servs", headers = { "version=1.0" })
public class ServController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ServService servService;

    @RequestMapping(value = "/{serv_id}/cfgs", method = RequestMethod.GET)
    @ResponseBody
    public Result list(@PathVariable("serv_id") String servId, HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            result = servService.listCfg(servId);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("查询服务配置异常：", e);
        }
        return result;
    }

}
