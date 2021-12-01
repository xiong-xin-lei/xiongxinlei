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

import com.bsg.dbscale.dbscale.annotation.OperateLog;
import com.bsg.dbscale.dbscale.annotation.UnderlineToCamel;
import com.bsg.dbscale.dbscale.query.NetworkQuery;
import com.bsg.dbscale.service.form.NetworkForm;
import com.bsg.dbscale.service.service.NetworkService;
import com.bsg.dbscale.service.service.Result;

@Controller
@RequestMapping(value = "networks", headers = { "version=1.0" })
@OperateLog(objType = "网段")
public class NetworkController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private NetworkService networkService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Result list(@UnderlineToCamel NetworkQuery networkQuery, HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            com.bsg.dbscale.service.query.NetworkQuery serviceQuery = new com.bsg.dbscale.service.query.NetworkQuery();
            serviceQuery.setSiteId(networkQuery.getSiteId());
            serviceQuery.setBusinessAreaId(networkQuery.getBusinessAreaId());
            serviceQuery.setTopology(networkQuery.getTopology());
            serviceQuery.setEnabled(networkQuery.getEnabled());
            result = networkService.list(serviceQuery);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("查询网段信息异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{network_id}", method = RequestMethod.GET)
    @ResponseBody
    public Result get(@PathVariable("network_id") String networkId, HttpServletResponse response) {
        Result result = null;
        try {
            result = networkService.get(networkId);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("查询网段详情异常：", e);
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @OperateLog(action = "新增")
    public Result save(@RequestBody NetworkForm networkForm, HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            result = networkService.save(networkForm);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_CREATED);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("新增网段异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{network_id}", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(action = "编辑")
    public Result update(@PathVariable("network_id") String networkId, @RequestBody NetworkForm networkForm,
            HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            result = networkService.update(networkId, networkForm);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("编辑业网段异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{network_id}/enabled", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(action = "启用")
    public Result enabled(@PathVariable("network_id") String networkId, HttpServletResponse response,
            HttpSession session) {
        Result result = null;
        try {
            result = networkService.enabled(networkId, true);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("启用网段异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{network_id}/disabled", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(action = "停用")
    public Result disabled(@PathVariable("network_id") String networkId, HttpServletResponse response,
            HttpSession session) {
        Result result = null;
        try {
            result = networkService.enabled(networkId, false);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("停用网段异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{network_id}", method = RequestMethod.DELETE)
    @ResponseBody
    @OperateLog(action = "删除")
    public Result remove(@PathVariable("network_id") String networkId, HttpServletResponse response,
            HttpSession session) {
        Result result = null;
        try {
            result = networkService.remove(networkId);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_NO_CONTENT);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("删除网段异常：", e);
        }
        return result;
    }
}
