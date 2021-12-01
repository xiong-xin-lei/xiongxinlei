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
import com.bsg.dbscale.dbscale.query.ClusterQuery;
import com.bsg.dbscale.service.form.ClusterForm;
import com.bsg.dbscale.service.service.ClusterService;
import com.bsg.dbscale.service.service.Result;

@Controller
@RequestMapping(value = "clusters", headers = { "version=1.0" })
@OperateLog(objType = "集群")
public class ClusterController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ClusterService clusterService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Result list(@UnderlineToCamel ClusterQuery clusterQuery, HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            com.bsg.dbscale.service.query.ClusterQuery serviceQuery = new com.bsg.dbscale.service.query.ClusterQuery();
            serviceQuery.setSiteId(clusterQuery.getSiteId());
            serviceQuery.setBusinessAreaId(clusterQuery.getBusinessAreaId());
            serviceQuery.setEnabled(clusterQuery.getEnabled());
            result = clusterService.list(serviceQuery);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("查询集群信息异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{cluster_id}", method = RequestMethod.GET)
    @ResponseBody
    public Result get(@PathVariable("cluster_id") String clusterId, HttpServletResponse response) {
        Result result = null;
        try {
            result = clusterService.get(clusterId);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("查询集群详情异常：", e);
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @OperateLog(action = "新增")
    public Result save(@RequestBody ClusterForm clusterForm, HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            result = clusterService.save(clusterForm);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_CREATED);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("新增集群异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{cluster_id}", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(action = "编辑")
    public Result update(@PathVariable("cluster_id") String clusterId, @RequestBody ClusterForm clusterForm,
            HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            result = clusterService.update(clusterId, clusterForm);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("编辑业集群异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{cluster_id}/enabled", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(action = "启用")
    public Result enabled(@PathVariable("cluster_id") String clusterId, HttpServletResponse response,
            HttpSession session) {
        Result result = null;
        try {
            result = clusterService.enabled(clusterId, true);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("启用集群异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{cluster_id}/disabled", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(action = "停用")
    public Result disabled(@PathVariable("cluster_id") String clusterId, HttpServletResponse response,
            HttpSession session) {
        Result result = null;
        try {
            result = clusterService.enabled(clusterId, false);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("停用集群异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{cluster_id}", method = RequestMethod.DELETE)
    @ResponseBody
    @OperateLog(action = "删除")
    public Result remove(@PathVariable("cluster_id") String clusterId, HttpServletResponse response,
            HttpSession session) {
        Result result = null;
        try {
            result = clusterService.remove(clusterId);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_NO_CONTENT);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("删除集群异常：", e);
        }
        return result;
    }
}
