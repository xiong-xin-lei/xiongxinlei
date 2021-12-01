package com.bsg.dbscale.dbscale.controller;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bsg.dbscale.service.service.Result;

@Controller
@RequestMapping(value = "health")
public class HealthController {

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Result health(HttpServletResponse response, HttpSession session) {
        return Result.success();
    }

}
