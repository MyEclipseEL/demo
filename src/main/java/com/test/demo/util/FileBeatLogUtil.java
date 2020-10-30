package com.test.demo.util;

import com.alibaba.fastjson.JSON;
import com.test.demo.util.wrapper.Wrapper;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.MDC;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import sun.rmi.runtime.Log;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class FileBeatLogUtil {

    public static void writeRequestInfo(HttpServletRequest request, String applicationName, String reqName, String requestParams) {
        String requestURI = request.getRequestURI();

        // 获取requestHeader
        Enumeration<String> requestHeaderNames = request.getHeaderNames();
        Map<String, Object> requestHeaderMap = new HashMap<>();
        while (requestHeaderNames.hasMoreElements()) {
            String name = requestHeaderNames.nextElement();
            String value = request.getHeaders(name).nextElement();
            requestHeaderMap.put(name, value);
        }
        String requestHeader = "";
        if (null != requestHeaderMap && requestHeaderMap.size() > 0) {
            requestHeader = JSON.toJSONString(requestHeaderMap);
        }
        applicationName = StringUtils.isEmpty(applicationName) ? "" : applicationName;
        requestURI = StringUtils.isEmpty(requestURI) ? "" : requestURI;
        reqName = StringUtils.isEmpty(reqName) ? "" : reqName;
        requestParams = "null".equals(requestParams) ? "" : requestParams;

        //MDC值为ES键值对JSON信息
        MDC.put("applicationName", applicationName);
        MDC.put("requestTime", getStringTodayTime());
        MDC.put("requestURI", requestURI);
        MDC.put("requestHeader", requestHeader);
        MDC.put("sourceName", reqName);
        MDC.put("requestParams", requestParams);
        MDC.put("exceptionCount", "0");
        // 防止空指针
    }

    public static void writeExceptionLog(String exceptionMethodName, String exceptionMethodArgs, String exceptionMessage) {

        MDC.put("exceptionCount", "1");
        exceptionMessage = String.format("MethodName:%s;Args:%s;Exception:%s", exceptionMethodName, exceptionMethodArgs, exceptionMessage);
        //MDC值为ES键值对JSON信息
        MDC.put("exceptionMessage", exceptionMessage);
        log.error("method: {}, error: {}", "writeExceptionLog", MDC.get("exceptionMessage"));
    }

    public static void writeResponseLog(Object o, Logger log, HttpServletResponse response) {

        //取responseHeader内容
        Map<String, Object> responseHeaderMap = new HashMap<>();
        Collection<String> headerNames = response.getHeaderNames();
        headerNames.forEach(name -> {
            responseHeaderMap.put(name, response.getHeader(name));
        });
        String strResponseHeader = "";
        if (null != responseHeaderMap && responseHeaderMap.size() > 0) {
            strResponseHeader = JSON.toJSONString(responseHeaderMap);
        }

        //获取response内容
        String responseCode = "";
        String responseMsg = "";
        String responseBody = "";
        Wrapper wrapper;
        if (null != o) {
            wrapper = (Wrapper) o;
            if (null != wrapper) {
                responseCode = String.valueOf(wrapper.getCode());
                responseMsg = wrapper.getMessage();
                Object result = wrapper.getResult();
                if (null != result) {
                    responseBody = result.toString();
                }
            }
        }


        //MDC值为ES键值对JSON信息
        MDC.put("responseHeader", strResponseHeader);
        MDC.put("responseCode", responseCode);
        MDC.put("responseMsg", responseMsg);
        MDC.put("responseBody", responseBody);
        MDC.put("responseTime", getStringTodayTime());

        Map<String, String> copyOfContextMap = MDC.getCopyOfContextMap();
        String reqInfoJsonStr = JSON.toJSONString(copyOfContextMap);
        log.info("method: {} , message: {}","writeResponseLog", reqInfoJsonStr);
    }

    /**
     * 获取请求参数，处理为json字符串
     *
     * @param joinPoint
     * @return
     */
    public static String getParams(JoinPoint joinPoint) {
        Object[] argValues = joinPoint.getArgs();
        String[] argNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
        if (argNames != null && argNames.length > 0) {
            for (int i = 0; i < argNames.length; i++) {
                String thisArgName = argNames[i];
                String thisArgValue = argValues[i].toString();
                linkedHashMap.put(thisArgName, thisArgValue);
            }
        }
        return JSON.toJSONString(linkedHashMap);
    }

    public static String getStringTodayTime() {
        Date todat_date = new Date();
        //将日期格式化
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        //转换成字符串格式
        return simpleDateFormat.format(todat_date);
    }

}
