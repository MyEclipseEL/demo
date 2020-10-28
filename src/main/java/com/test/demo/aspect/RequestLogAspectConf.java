package com.test.demo.aspect;

import com.test.demo.util.FileBeatLogUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Order(0)
@Component
@Slf4j
public class RequestLogAspectConf {

    @Autowired
    private Environment environment;

    @Autowired
    private HttpServletRequest request;


    @Pointcut("execution(* com.test.demo.controller..*.*(..))")
    public void methodPointCut() {}

    @Before("methodPointCut()")
    void doBefore(JoinPoint joinPoint) {
        authLogic(joinPoint);
    }


    private void authLogic(JoinPoint joinPoint) {
        try {
            String applicationName = environment.getProperty("spring.application.name");

            // 获取当前http请求
            String reqName = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();

            String requestParams = FileBeatLogUtil.getParams(joinPoint);

            FileBeatLogUtil.writeRequestInfo(request, applicationName, reqName, requestParams);

        } catch (Exception e) {
            log.error("RequestLogAspectConf;authLogic;Exception{}", e.getMessage());
        }
    }


}
