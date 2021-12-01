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
import com.bsg.dbscale.dbscale.query.StorageQuery;
import com.bsg.dbscale.service.form.PVCStorageForm;
import com.bsg.dbscale.service.service.PVCStorageService;
import com.bsg.dbscale.service.service.Result;

@Controller
@RequestMapping(value = "pvc_storages", headers = { "version=1.0" })
@OperateLog(objType = "PVC存储")
public class PVCStorageController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private PVCStorageService storageService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Result list(@UnderlineToCamel StorageQuery storageQuery, HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            com.bsg.dbscale.service.query.PVCStorageQuery serviceQuery = new com.bsg.dbscale.service.query.PVCStorageQuery();
            serviceQuery.setSiteId(storageQuery.getSiteId());
            serviceQuery.setEnabled(storageQuery.getEnabled());
            result = storageService.list(serviceQuery);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("查询PVC存储信息异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{storage_id}", method = RequestMethod.GET)
    @ResponseBody
    public Result get(@PathVariable("storage_id") String storageId, HttpServletResponse response) {
        Result result = null;
        try {
            result = storageService.get(storageId);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("查询PVC存储详情异常：", e);
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @OperateLog(action = "新增")
    public Result save(@RequestBody PVCStorageForm pvcStorageForm, HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            result = storageService.save(pvcStorageForm);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_CREATED);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("新增PVC存储异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{storage_id}", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(action = "编辑")
    public Result update(@PathVariable("storage_id") String storageId, @RequestBody PVCStorageForm pvcStorageForm,
            HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            result = storageService.update(storageId, pvcStorageForm);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("编辑业PVC存储异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{storage_id}/enabled", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(action = "启用")
    public Result enabled(@PathVariable("storage_id") String storageId, HttpServletResponse response,
            HttpSession session) {
        Result result = null;
        try {
            result = storageService.enabled(storageId, true);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("启用PVC存储异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{storage_id}/disabled", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(action = "停用")
    public Result disabled(@PathVariable("storage_id") String storageId, HttpServletResponse response,
            HttpSession session) {
        Result result = null;
        try {
            result = storageService.enabled(storageId, false);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("停用PVC存储异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{storage_id}", method = RequestMethod.DELETE)
    @ResponseBody
    @OperateLog(action = "删除")
    public Result remove(@PathVariable("storage_id") String storageId, HttpServletResponse response,
            HttpSession session) {
        Result result = null;
        try {
            result = storageService.remove(storageId);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_NO_CONTENT);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("删除PVC存储异常：", e);
        }
        return result;
    }
}
