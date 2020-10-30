package com.test.demo.aspect;

import com.test.demo.util.FileBeatLogUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Order(1)
@Component
@Slf4j(topic = "logstashErrorLog")
public class ExceptionLogAspect {
    /**
     * 范围切点方法
     */
    @Pointcut("execution(* com.test.demo..*.*(..))")
    public void methodPointCut() {
    }

    @AfterThrowing(throwing = "ex", pointcut = "methodPointCut()")
    public void throwss(JoinPoint joinPoint, Exception ex) {
        try {
            log.info("throws :\n");
            String methodArgs = Arrays.toString(joinPoint.getArgs());
            FileBeatLogUtil.writeExceptionLog(joinPoint.getSignature().toString(), methodArgs, ex.getMessage());
        } catch (Exception e) {
            log.error("ExceptionLogAspect;writeExceptionLog;Exception:{}", e.getMessage());
        }
    }
}
