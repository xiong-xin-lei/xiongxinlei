package com.bsg.dbscale.dbscale.controller;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.bsg.dbscale.cm.model.CmVersion;
import com.bsg.dbscale.dao.domain.UserDO;
import com.bsg.dbscale.service.service.Result;
import com.bsg.dbscale.service.service.SysService;

@Controller
@RequestMapping(value = "sys", headers = { "version=1.0" })
public class SysController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SysService sysService;

    private static final String VERSION = "v3.0.0";
    private static final String VERSION_PRERELEASE = "dev";

    @RequestMapping(value = "/version", method = RequestMethod.GET)
    @ResponseBody
    public Result getVersion(HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            JSONObject resultJson = new JSONObject();
            ClassPathResource resource = new ClassPathResource("git.properties");
            CmVersion cmVersion = sysService.getVersion();
            resultJson.put("cmVersion", cmVersion);
            if (resource != null) {
                String str = FileUtils.readFileToString(resource.getFile(), "utf-8");
                JSONObject jsonObject = JSONObject.parseObject(str);

                JSONObject webVersion = new JSONObject();
                resultJson.put("webVersion", webVersion);
                String version = VERSION;
                if (StringUtils.isNotBlank(VERSION_PRERELEASE)) {
                    version = version + "-" + VERSION_PRERELEASE;
                }
                webVersion.put("version", version);
                webVersion.put("commitId", jsonObject.get("git.commit.id.abbrev"));
                webVersion.put("buildTime", jsonObject.get("git.build.time"));
            }
            result = Result.success(resultJson);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("查询系统版本异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/session/user", method = RequestMethod.GET)
    @ResponseBody
    public Result sessionUser(HttpSession session, HttpServletResponse response) {
        Result result = null;
        try {
            UserDO userDO = sysService.getActiveUser(session);
            result = Result.success(userDO);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("查询session用户异常：", e);
        }
        return result;
    }

}
