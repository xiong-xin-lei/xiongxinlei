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

import com.bsg.dbscale.dao.domain.UserDO;
import com.bsg.dbscale.dbscale.annotation.OperateLog;
import com.bsg.dbscale.dbscale.annotation.UnderlineToCamel;
import com.bsg.dbscale.service.form.HostForm;
import com.bsg.dbscale.service.query.HostQuery;
import com.bsg.dbscale.service.service.HostService;
import com.bsg.dbscale.service.service.Result;

@Controller
@RequestMapping(value = "hosts", headers = { "version=1.0" })
@OperateLog(objType = "主机")
public class HostController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private HostService hostService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Result list(@UnderlineToCamel HostQuery hostQuery, HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            com.bsg.dbscale.service.query.HostQuery serviceQuery = new com.bsg.dbscale.service.query.HostQuery();
            serviceQuery.setSiteId(hostQuery.getSiteId());
            serviceQuery.setClusterId(hostQuery.getClusterId());
            serviceQuery.setEnabled(hostQuery.getEnabled());
            serviceQuery.setDiskType(hostQuery.getDiskType());
            serviceQuery.setState(hostQuery.getState());
            result = hostService.list(serviceQuery);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("查询主机信息异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{host_id_or_ip:.+}", method = RequestMethod.GET)
    @ResponseBody
    public Result get(@PathVariable("host_id_or_ip") String hostIdOrIp, HttpServletResponse response) {
        Result result = null;
        try {
            result = hostService.get(hostIdOrIp);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("查询主机详情异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{host_id}/events", method = RequestMethod.GET)
    @ResponseBody
    public Result getEvent(@PathVariable("host_id") String hostId, HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            result = hostService.getEvent(hostId);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("查询主机事件异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{host_id}/monitor", method = RequestMethod.GET)
    @ResponseBody
    public Result monitor(@PathVariable("host_id") String hostId, HttpServletResponse response) {
        Result result = null;
        try {
            result = hostService.monitor(hostId);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("主机监控异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{host_id}/units", method = RequestMethod.GET)
    @ResponseBody
    public Result listUnits(@PathVariable("host_id") String hostId, HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            result = hostService.listUnit(hostId);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("查询主机单元异常：", e);
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @OperateLog(action = "注册")
    public Result save(@RequestBody HostForm hostForm, HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            UserDO userDO = hostService.getActiveUser(session);
            String activeUsername = userDO.getUsername();

            result = hostService.save(hostForm, activeUsername);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_CREATED);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("新增主机异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{host_id}", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(action = "编辑")
    public Result update(@PathVariable("host_id") String hostId, @RequestBody HostForm hostForm,
            HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            result = hostService.update(hostId, hostForm);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("主机编辑异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{host_id}/in", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(action = "入库")
    public Result in(@PathVariable("host_id") String hostId, HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            UserDO userDO = hostService.getActiveUser(session);
            String activeUsername = userDO.getUsername();

            result = hostService.in(hostId, activeUsername);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("主机入库异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{host_id}/out", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(action = "出库")
    public Result out(@PathVariable("host_id") String hostId, HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            UserDO userDO = hostService.getActiveUser(session);
            String activeUsername = userDO.getUsername();

            result = hostService.out(hostId, activeUsername);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("主机出库异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{host_id}/enabled", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(action = "启用")
    public Result enabled(@PathVariable("host_id") String hostId, HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            result = hostService.enabled(hostId, true);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("启用主机异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{host_id}/disabled", method = RequestMethod.PUT)
    @ResponseBody
    @OperateLog(action = "停用")
    public Result disabled(@PathVariable("host_id") String hostId, HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            result = hostService.enabled(hostId, false);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("停用主机异常：", e);
        }
        return result;
    }

    @RequestMapping(value = "/{host_id}", method = RequestMethod.DELETE)
    @ResponseBody
    @OperateLog(action = "注销")
    public Result remove(@PathVariable("host_id") String hostId, HttpServletResponse response, HttpSession session) {
        Result result = null;
        try {
            result = hostService.remove(hostId);
            if (result.getCode() != Result.SUCCESS) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return result;
            }
            response.setStatus(HttpStatus.SC_NO_CONTENT);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result = Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统异常：" + e.getMessage());
            logger.error("注销主机异常：", e);
        }
        return result;
    }
}
