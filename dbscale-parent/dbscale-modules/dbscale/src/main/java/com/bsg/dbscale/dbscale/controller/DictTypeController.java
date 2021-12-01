package com.bsg.dbscale.dbscale.controller;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bsg.dbscale.service.service.DictTypeService;
import com.bsg.dbscale.service.service.Result;

@Controller
@RequestMapping(value = "dict_types", headers = { "version=1.0" })
public class DictTypeController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private DictTypeService dictTypeService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Result list(HttpServletResponse response) {
        Result result = null;
        try {
            result = dictTypeService.list();
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("查询字典信息异常：", e);
        }
        return result;
    }

}
