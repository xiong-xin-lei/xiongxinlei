package com.bsg.dbscale.dbscale.controller;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bsg.dbscale.service.service.DefServService;
import com.bsg.dbscale.service.service.Result;

@Controller
@RequestMapping(value = "def_servs", headers = { "version=1.0" })
public class DefServController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private DefServService defServService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Result list(@RequestParam(name = "enabled", required = false) Boolean enabled,
            HttpServletResponse response) {
        Result result = null;
        try {
            result = defServService.list(enabled);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("查询服务定义信息异常：", e);
        }
        return result;
    }
}
