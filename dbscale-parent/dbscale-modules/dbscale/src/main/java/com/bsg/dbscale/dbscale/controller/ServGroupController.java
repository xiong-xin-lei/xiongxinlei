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
import com.bsg.dbscale.dbscale.annotation.OperateLog;
import com.bsg.dbscale.service.constant.Consts;
import com.bsg.dbscale.service.form.ParamCfgForm;
import com.bsg.dbscale.service.form.ServArchUpForm;
import com.bsg.dbscale.service.form.ServImageForm;
import com.bsg.dbscale.service.form.ServScaleCpuMemForm;
import com.bsg.dbscale.service.form.ServScaleStorageForm;
import com.bsg.dbscale.service.service.BaseService.ObjModel;
import com.bsg.dbscale.service.service.CmhaServGroupService;
import com.bsg.dbscale.service.service.MysqlServGroupService;
import com.bsg.dbscale.service.service.RedisServGroupService;
import com.bsg.dbscale.service.service.Result;
import com.bsg.dbscale.service.service.ServGroupService;

@Controller
@RequestMapping(value = "serv_groups", headers = { "version=1.0" })
@OperateLog(objType = "服务")
public class ServGroupController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ServGroupService servGroupService;

    @Autowired
    private MysqlServGroupService mysqlServGroupService;
    
    @Autowired
    private CmhaServGroupService cmhaServGroupService;

    @Autowired
    private RedisServGroupService redisServGroupService;

    @RequestMapping(value = "/{serv_group_id}/events", method = RequestMethod.GET)
    @ResponseBody
    public Result list(@PathVariable("serv_group_id") String servGroupId, HttpServletResponse response,
            HttpSession session) {
        Result result = null;
        try {
            result = servGroupService.getEvent(servGroupId);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("查询服务组事件异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{serv_group_id}/start", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(action = "启动")
    public Result start(@PathVariable("serv_group_id") String servGroupId, HttpServletResponse response,
            HttpSession session) {
        Result result = null;
        try {
            UserDO userDO = servGroupService.getActiveUser(session);
            String activeUsername = userDO.getUsername();

            ObjModel objModel = servGroupService.getObjModel(servGroupId);
            if (objModel != null) {
                switch (objModel.getType()) {
                case Consts.SERV_GROUP_TYPE_MYSQL:
                    result = mysqlServGroupService.start(servGroupId, activeUsername);
                    break;
                case Consts.SERV_GROUP_TYPE_CMHA:
                    result = cmhaServGroupService.start(servGroupId, activeUsername);
                    break;
                case Consts.SERV_GROUP_TYPE_REDIS:
                    result = redisServGroupService.start(servGroupId, activeUsername);
                    break;
                default:
                    break;
                }
            }
            if (result != null && result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("启动服务组异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{serv_group_id}/stop", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(action = "停止")
    public Result stop(@PathVariable("serv_group_id") String servGroupId, HttpServletResponse response,
            HttpSession session) {
        Result result = null;
        try {
            UserDO userDO = servGroupService.getActiveUser(session);
            String activeUsername = userDO.getUsername();

            ObjModel objModel = servGroupService.getObjModel(servGroupId);
            if (objModel != null) {
                switch (objModel.getType()) {
                case Consts.SERV_GROUP_TYPE_MYSQL:
                    result = mysqlServGroupService.stop(servGroupId, activeUsername);
                    break;
                case Consts.SERV_GROUP_TYPE_CMHA:
                    result = cmhaServGroupService.stop(servGroupId, activeUsername);
                    break;
                case Consts.SERV_GROUP_TYPE_REDIS:
                    result = redisServGroupService.stop(servGroupId, activeUsername);
                    break;
                default:
                    break;
                }
            }
            if (result != null && result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("停止服务组异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{serv_group_id}/images", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(action = "升级")
    public Result imageUpdate(@PathVariable("serv_group_id") String servGroupId, @RequestBody ServImageForm imageForm,
            HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            UserDO userDO = servGroupService.getActiveUser(session);
            String activeUsername = userDO.getUsername();

            ObjModel objModel = servGroupService.getObjModel(servGroupId);
            if (objModel != null) {
                switch (objModel.getType()) {
                case Consts.SERV_GROUP_TYPE_MYSQL:
                    result = mysqlServGroupService.imageUpdate(servGroupId, imageForm, activeUsername);
                    break;
                case Consts.SERV_GROUP_TYPE_CMHA:
                    result = cmhaServGroupService.imageUpdate(servGroupId, imageForm, activeUsername);
                    break;
                case Consts.SERV_GROUP_TYPE_REDIS:
                    result = redisServGroupService.imageUpdate(servGroupId, imageForm, activeUsername);
                    break;
                default:
                    break;
                }
            }
            if (result != null && result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("升级服务组异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{serv_group_id}/scale/cpumem", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(action = "计算扩容")
    public Result scaleUpCpuMem(@PathVariable("serv_group_id") String servGroupId,
            @RequestBody ServScaleCpuMemForm scaleCpuMemForm, HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            UserDO userDO = servGroupService.getActiveUser(session);
            String activeUsername = userDO.getUsername();

            ObjModel objModel = servGroupService.getObjModel(servGroupId);
            if (objModel != null) {
                switch (objModel.getType()) {
                case Consts.SERV_GROUP_TYPE_MYSQL:
                    result = mysqlServGroupService.scaleUpCpuMem(servGroupId, scaleCpuMemForm, activeUsername);
                    break;
                case Consts.SERV_GROUP_TYPE_CMHA:
                    result = cmhaServGroupService.scaleUpCpuMem(servGroupId, scaleCpuMemForm, activeUsername);
                    break;
                case Consts.SERV_GROUP_TYPE_REDIS:
                    result = redisServGroupService.scaleUpCpuMem(servGroupId, scaleCpuMemForm, activeUsername);
                    break;
                default:
                    break;
                }
            }
            if (result != null && result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("扩容服务组异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{serv_group_id}/scale/storages", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(action = "存储扩容")
    public Result scaleUpStorage(@PathVariable("serv_group_id") String servGroupId,
            @RequestBody ServScaleStorageForm scaleStorageForm, HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            UserDO userDO = servGroupService.getActiveUser(session);
            String activeUsername = userDO.getUsername();

            ObjModel objModel = servGroupService.getObjModel(servGroupId);
            if (objModel != null) {
                switch (objModel.getType()) {
                case Consts.SERV_GROUP_TYPE_MYSQL:
                    result = mysqlServGroupService.scaleUpStorage(servGroupId, scaleStorageForm, activeUsername);
                    break;
                case Consts.SERV_GROUP_TYPE_CMHA:
                    result = cmhaServGroupService.scaleUpStorage(servGroupId, scaleStorageForm, activeUsername);
                    break;
                case Consts.SERV_GROUP_TYPE_REDIS:
                    result = redisServGroupService.scaleUpStorage(servGroupId, scaleStorageForm, activeUsername);
                    break;
                default:
                    break;
                }
            }
            if (result != null && result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("扩容服务组异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{serv_group_id}/archs", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(action = "节点扩展")
    public Result archUp(@PathVariable("serv_group_id") String servGroupId, @RequestBody ServArchUpForm archUpForm,
            HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            UserDO userDO = servGroupService.getActiveUser(session);
            String activeUsername = userDO.getUsername();

            ObjModel objModel = servGroupService.getObjModel(servGroupId);
            if (objModel != null) {
                switch (objModel.getType()) {
                case Consts.SERV_GROUP_TYPE_MYSQL:
                    result = mysqlServGroupService.archUp(servGroupId, archUpForm, activeUsername);
                    break;
                case Consts.SERV_GROUP_TYPE_CMHA:
                    result = cmhaServGroupService.archUp(servGroupId, archUpForm, activeUsername);
                    break;
                case Consts.SERV_GROUP_TYPE_REDIS:
                    result = redisServGroupService.archUp(servGroupId, archUpForm, activeUsername);
                    break;
                default:
                    break;
                }
            }
            if (result != null && result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("节点扩展异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{serv_group_id}", method = RequestMethod.DELETE)
    @ResponseBody
    @OperateLog(action = "删除")
    public Result remove(@PathVariable("serv_group_id") String servGroupId, HttpServletResponse response,
            HttpSession session) {
        Result result = null;
        try {
            UserDO userDO = servGroupService.getActiveUser(session);
            String activeUsername = userDO.getUsername();

            ObjModel objModel = servGroupService.getObjModel(servGroupId);
            if (objModel != null) {
                switch (objModel.getType()) {
                case Consts.SERV_GROUP_TYPE_MYSQL:
                    result = mysqlServGroupService.remove(servGroupId, activeUsername);
                    break;
                case Consts.SERV_GROUP_TYPE_CMHA:
                    result = cmhaServGroupService.remove(servGroupId, activeUsername);
                    break;
                case Consts.SERV_GROUP_TYPE_REDIS:
                    result = redisServGroupService.remove(servGroupId, activeUsername);
                    break;
                default:
                    break;
                }
            }
            if (result != null && result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_NO_CONTENT);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("删除服务组异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{serv_group_id}/cfgs", method = RequestMethod.GET)
    @ResponseBody
    public Result listCfg(@PathVariable("serv_group_id") String servGroupId, @RequestParam("type") String type,
            HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            result = servGroupService.listCfg(servGroupId, type);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("查询服务配置异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{serv_group_id}/cfgs", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(objType = "参数配置", action = "编辑")
    public Result updateCfg(@PathVariable("serv_group_id") String servGroupId, @RequestParam("type") String type,
            @RequestBody ParamCfgForm paramCfgForm, HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            result = servGroupService.updateCfg(servGroupId, type, paramCfgForm);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("更新服务参数配置异常：", e);
        }
        return result;
    }
}
