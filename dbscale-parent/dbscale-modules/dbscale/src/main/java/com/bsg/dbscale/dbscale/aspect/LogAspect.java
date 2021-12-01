package com.bsg.dbscale.dbscale.aspect;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 日志切面类
 * 
 * @author HCK
 *
 */
@Aspect
@Component
public class LogAspect {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Pointcut("execution(public * com.bsg.dbscale.dbscale.controller.*.*(..))")
    public void pointcut() {

    }

    @Around("pointcut()")
    public Object doAround(ProceedingJoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        String params = "";
        long startTime = System.currentTimeMillis();
        Object[] args = joinPoint.getArgs();
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                if (!(args[i] instanceof HttpServletResponse)) {
                    params += args[i] + ";";
                }
            }
        }

        logger.info("WEB 请求地址: {}", request.getServletPath());
        logger.info("WEB 请求方法: {}", request.getMethod());
        logger.info("WEB 请求参数: {}", params);

        Object rvt = null;
        try {
            rvt = joinPoint.proceed(args);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        logger.debug("WEB 响应结果: {}", rvt);

        long endTime = System.currentTimeMillis();
        logger.info("WEB 响应耗时: {}ms", endTime - startTime);

        return rvt;
    }
}
