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
import com.bsg.dbscale.service.form.DictForm;
import com.bsg.dbscale.service.service.DictService;
import com.bsg.dbscale.service.service.Result;

@Controller
@RequestMapping(value = "dicts", headers = { "version=1.0" })
public class DictController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private DictService dictService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Result list(@RequestParam("dict_type_code") String dictTypeCode, HttpServletResponse response) {
        Result result = null;
        try {
            result = dictService.list(dictTypeCode);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("查询字典信息异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{dict_code}", method = RequestMethod.GET)
    @ResponseBody
    public Result list(@PathVariable("dict_code") String dictCode, @RequestParam("dict_type_code") String dictTypeCode,
            HttpServletResponse response) {
        Result result = null;
        try {
            result = dictService.get(dictTypeCode, dictCode);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("查询字典详情异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{dict_code}", method = RequestMethod.PUT)
    @ResponseBody
    public Result update(@PathVariable("dict_code") String dictCode,
            @RequestParam("dict_type_code") String dictTypeCode, @RequestBody DictForm dictForm,
            HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            UserDO userDO = dictService.getActiveUser(session);
            String activeUsername = userDO.getUsername();

            result = dictService.update(dictTypeCode, dictCode, dictForm, activeUsername);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("编辑字典异常：", e);
        }
        return result;
    }

}
