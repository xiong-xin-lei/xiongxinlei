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
import com.bsg.dbscale.dbscale.query.BackupFileQuery;
import com.bsg.dbscale.service.form.BackupExternalForm;
import com.bsg.dbscale.service.service.BackupFileService;
import com.bsg.dbscale.service.service.Result;

@Controller
@RequestMapping(value = "backup/files", headers = { "version=1.0" })
@OperateLog(objType = "备份文件")
public class BackupFileController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private BackupFileService backupFileService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Result list(@UnderlineToCamel BackupFileQuery backupFileQuery, HttpServletResponse response,
            HttpSession session) {
        Result result = null;
        try {
            com.bsg.dbscale.service.query.BackupFileQuery serviceQuery = new com.bsg.dbscale.service.query.BackupFileQuery();
            serviceQuery.setSiteId(backupFileQuery.getSiteId());
            serviceQuery.setServGroupId(backupFileQuery.getServGroupId());
            serviceQuery.setUnitId(backupFileQuery.getUnitId());
            serviceQuery.setSuccess(backupFileQuery.getSuccess());
            serviceQuery.setExternal(backupFileQuery.getExternal());
            serviceQuery.setType(backupFileQuery.getType());
            result = backupFileService.list(serviceQuery);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("查询备份文件信息异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{file_id}", method = RequestMethod.DELETE)
    @ResponseBody
    @OperateLog(action = "删除")
    public Result remove(@PathVariable("file_id") String backupFileId, HttpServletResponse response,
            HttpSession session) {
        Result result = null;
        try {
            result = backupFileService.remove(backupFileId);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_NO_CONTENT);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("删除备份文件异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/external", method = RequestMethod.POST)
    @ResponseBody
    @OperateLog(action = "新增")
    public Result save(@RequestBody BackupExternalForm backupExternalForm, HttpServletResponse response,
            HttpSession session) {
        Result result = null;
        try {
            result = backupFileService.saveExternalFile(backupExternalForm);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_CREATED);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("新增外部备份文件异常：", e);
        }
        return result;
    }
}
