package com.test.demo.aspect;

import com.test.demo.util.FileBeatLogUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
@Slf4j
public class ResponseLogAdvice implements ResponseBodyAdvice {

    @Autowired
    private HttpServletResponse response;

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        try {

            if (o != null) {

                Logger log = LoggerFactory.getLogger("logstashInfo");

                FileBeatLogUtil.writeResponseLog(o, log, response);

            }
        } catch (Exception e) {
            log.error("beforeBodyWrite;Exception:{}", e.getMessage());
        }
        return o;
    }
}
