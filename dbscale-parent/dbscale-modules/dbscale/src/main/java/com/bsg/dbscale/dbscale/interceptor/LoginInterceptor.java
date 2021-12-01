package com.bsg.dbscale.dbscale.interceptor;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.bsg.dbscale.dao.domain.UserDO;

public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");

        HttpSession session = request.getSession();
        UserDO userDO = (UserDO) session.getAttribute("user");
        
        String requestUrl=request.getServletPath();
        String version=request.getHeader("version");
        String projectName=request.getContextPath();
        
        if("/app/login".equals(requestUrl)){
            return true;
        }
        if (userDO == null) {
            response.setStatus(HttpStatus.SC_UNAUTHORIZED);
            if(StringUtils.isBlank(version)){
                response.sendRedirect(projectName+"/app/login");
            }
            PrintWriter out = response.getWriter();
            out.write("未授权登录");
            out.close();
            return false;
        }
        return true;
    }

}
