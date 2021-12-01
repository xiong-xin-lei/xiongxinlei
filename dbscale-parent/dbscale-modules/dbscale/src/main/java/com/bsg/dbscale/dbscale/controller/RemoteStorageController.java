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
import com.bsg.dbscale.service.form.RemoteStorageForm;
import com.bsg.dbscale.service.form.RemoteStoragePoolForm;
import com.bsg.dbscale.service.service.RemoteStoragePoolService;
import com.bsg.dbscale.service.service.RemoteStorageService;
import com.bsg.dbscale.service.service.Result;

@Controller
@RequestMapping(value = "remote_storages", headers = { "version=1.0" })
@OperateLog(objType = "外置存储")
public class RemoteStorageController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RemoteStorageService remoteStorageService;

    @Autowired
    private RemoteStoragePoolService remoteStoragePoolService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Result list(@UnderlineToCamel StorageQuery remoteStorageQuery, HttpServletResponse response,
            HttpSession session) {
        Result result = null;
        try {
            com.bsg.dbscale.service.query.RemoteStorageQuery serviceQuery = new com.bsg.dbscale.service.query.RemoteStorageQuery();
            serviceQuery.setSiteId(remoteStorageQuery.getSiteId());
            serviceQuery.setEnabled(remoteStorageQuery.getEnabled());
            result = remoteStorageService.list(serviceQuery);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("查询外置存储信息异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{remote_storage_id}", method = RequestMethod.GET)
    @ResponseBody
    public Result get(@PathVariable("remote_storage_id") String remoteStorageId, HttpServletResponse response) {
        Result result = null;
        try {
            result = remoteStorageService.get(remoteStorageId);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("查询外置存储详情异常：", e);
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @OperateLog(action = "新增")
    public Result save(@RequestBody RemoteStorageForm remoteStorageForm, HttpServletResponse response,
            HttpSession session) {
        Result result = null;
        try {
            result = remoteStorageService.save(remoteStorageForm);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_CREATED);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("新增外置存储异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{remote_storage_id}", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(action = "编辑")
    public Result update(@PathVariable("remote_storage_id") String remoteStorageId,
            @RequestBody RemoteStorageForm remoteStorageForm, HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            result = remoteStorageService.update(remoteStorageId, remoteStorageForm);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("编辑业外置存储异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{remote_storage_id}/enabled", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(action = "启用")
    public Result enabled(@PathVariable("remote_storage_id") String remoteStorageId, HttpServletResponse response,
            HttpSession session) {
        Result result = null;
        try {
            result = remoteStorageService.enabled(remoteStorageId, true);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("启用外置存储异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{remote_storage_id}/disabled", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(action = "停用")
    public Result disabled(@PathVariable("remote_storage_id") String remoteStorageId, HttpServletResponse response,
            HttpSession session) {
        Result result = null;
        try {
            result = remoteStorageService.enabled(remoteStorageId, false);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("停用外置存储异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{remote_storage_id}", method = RequestMethod.DELETE)
    @ResponseBody
    @OperateLog(action = "删除")
    public Result remove(@PathVariable("remote_storage_id") String remoteStorageId, HttpServletResponse response,
            HttpSession session) {
        Result result = null;
        try {
            result = remoteStorageService.remove(remoteStorageId);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_NO_CONTENT);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("删除外置存储异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{remote_storage_id}/pools", method = RequestMethod.GET)
    @ResponseBody
    public Result listPool(@PathVariable("remote_storage_id") String remoteStorageId, HttpServletResponse response,
            HttpSession session) {
        Result result = null;
        try {
            result = remoteStoragePoolService.list(remoteStorageId);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("查询外置存储池信息异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{remote_storage_id}/pools/{pool_id}", method = RequestMethod.GET)
    @ResponseBody
    public Result getPool(@PathVariable("remote_storage_id") String remoteStorageId,
            @PathVariable("pool_id") String poolId, HttpServletResponse response) {
        Result result = null;
        try {
            result = remoteStoragePoolService.get(remoteStorageId, poolId);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("查询外置存储池详情异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{remote_storage_id}/pools", method = RequestMethod.POST)
    @ResponseBody
    @OperateLog(objType = "外置存储池", action = "新增")
    public Result savePool(@PathVariable("remote_storage_id") String remoteStorageId,
            @RequestBody RemoteStoragePoolForm remoteStoragePoolForm, HttpServletResponse response,
            HttpSession session) {
        Result result = null;
        try {
            result = remoteStoragePoolService.save(remoteStorageId, remoteStoragePoolForm);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_CREATED);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("新增外置存储池异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{remote_storage_id}/pools/{pool_id}", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(objType = "外置存储池", action = "编辑")
    public Result updatePool(@PathVariable("remote_storage_id") String remoteStorageId,
            @PathVariable("pool_id") String poolId, @RequestBody RemoteStoragePoolForm remoteStoragePoolForm,
            HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            result = remoteStoragePoolService.update(remoteStorageId, poolId, remoteStoragePoolForm);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("编辑业外置存储池异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{remote_storage_id}/pools/{pool_id}/enabled", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(objType = "外置存储池", action = "启用")
    public Result enabledPool(@PathVariable("remote_storage_id") String remoteStorageId,
            @PathVariable("pool_id") String poolId, HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            result = remoteStoragePoolService.enabled(remoteStorageId, poolId, true);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("启用外置存储池异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{remote_storage_id}/pools/{pool_id}/disabled", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(objType = "外置存储池", action = "停用")
    public Result disabled(@PathVariable("remote_storage_id") String remoteStorageId,
            @PathVariable("pool_id") String poolId, HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            result = remoteStoragePoolService.enabled(remoteStorageId, poolId, false);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("停用外置存储池异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{remote_storage_id}/pools/{pool_id}", method = RequestMethod.DELETE)
    @ResponseBody
    @OperateLog(objType = "外置存储池", action = "删除")
    public Result removePool(@PathVariable("remote_storage_id") String remoteStorageId,
            @PathVariable("pool_id") String poolId, HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            result = remoteStoragePoolService.remove(remoteStorageId, poolId);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_NO_CONTENT);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("删除外置存储池异常：", e);
        }
        return result;
    }
}
