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

import com.bsg.dbscale.dbscale.annotation.OperateLog;
import com.bsg.dbscale.dbscale.annotation.UnderlineToCamel;
import com.bsg.dbscale.service.form.ImageForm;
import com.bsg.dbscale.service.form.ImageTemplateForm;
import com.bsg.dbscale.service.query.ImageQuery;
import com.bsg.dbscale.service.service.ImageService;
import com.bsg.dbscale.service.service.Result;

@Controller
@RequestMapping(value = "images", headers = { "version=1.0" })
@OperateLog(objType = "镜像")
public class ImageController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ImageService imageService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Result list(@UnderlineToCamel ImageQuery imageQuery, HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            com.bsg.dbscale.service.query.ImageQuery serviceQuery = new com.bsg.dbscale.service.query.ImageQuery();
            serviceQuery.setSiteId(imageQuery.getSiteId());
            serviceQuery.setType(imageQuery.getType());
            serviceQuery.setArchitecture(imageQuery.getArchitecture());
            serviceQuery.setEnabled(imageQuery.getEnabled());
            serviceQuery.setMajor(imageQuery.getMajor());
            serviceQuery.setMinor(imageQuery.getMinor());
            result = imageService.list(serviceQuery);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("查询镜像信息异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{image_id:.+}", method = RequestMethod.GET)
    @ResponseBody
    public Result get(@PathVariable("image_id") String imageId, HttpServletResponse response) {
        Result result = null;
        try {
            result = imageService.get(imageId);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("查询镜像详情异常：", e);
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @OperateLog(action = "注册")
    public Result save(@RequestBody ImageForm imageForm, HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            result = imageService.save(imageForm);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("镜像注册异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{image_id:.+}/enabled", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(action = "启用")
    public Result enabled(@PathVariable("image_id") String imageId, HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            result = imageService.enabled(imageId, true);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("启用镜像异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{image_id:.+}/disabled", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(action = "停用")
    public Result disabled(@PathVariable("image_id") String imageId, HttpServletResponse response,
            HttpSession session) {
        Result result = null;
        try {
            result = imageService.enabled(imageId, false);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("停用镜像异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{image_id:.+}", method = RequestMethod.DELETE)
    @ResponseBody
    @OperateLog(action = "注销")
    public Result remove(@PathVariable("image_id") String imageId, HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            result = imageService.remove(imageId);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_NO_CONTENT);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("注销镜像异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{type}/templates", method = RequestMethod.GET)
    @ResponseBody
    public Result listImageTamplate(@PathVariable("type") String type, @RequestParam("site_id") String siteId,
            @RequestParam("major") Integer major, @RequestParam("minor") Integer minor,
            @RequestParam("patch") Integer patch, @RequestParam("build") Integer build,
            @RequestParam("architecture") String architecture, HttpServletResponse response) {
        Result result = null;
        try {
            result = imageService.listTempate(siteId, type, major, minor, patch, build, architecture);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("查询镜像模板异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{type}/templates", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(objType = "镜像模板", action = "编辑")
    public Result updateImageTamplate(@PathVariable("type") String type, @RequestParam("site_id") String siteId,
            @RequestParam("major") Integer major, @RequestParam("minor") Integer minor,
            @RequestParam("patch") Integer patch, @RequestParam("build") Integer build,
            @RequestParam("architecture") String architecture, @RequestBody ImageTemplateForm imageTemplateForm,
            HttpServletResponse response) {
        Result result = null;
        try {
            result = imageService.updateTempate(siteId, type, major, minor, patch, build, architecture,
                    imageTemplateForm);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("编辑镜像模板异常：", e);
        }
        return result;
    }
}
