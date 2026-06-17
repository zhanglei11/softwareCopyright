package com.sva.framework.aspect;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sva.framework.security.LoginUser;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.annotation.*;

@Aspect
@Component
public class OperationLogAspect {

    private static final Logger log = LoggerFactory.getLogger(OperationLogAspect.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Around("@annotation(operLog)")
    public Object around(ProceedingJoinPoint point, OperLog operLog) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = null;
        int status = 1;
        try {
            result = point.proceed();
            return result;
        } catch (Exception e) {
            status = 0;
            throw e;
        } finally {
            try {
                saveLog(point, operLog, result, status, System.currentTimeMillis() - startTime);
            } catch (Exception ex) {
                log.error("记录操作日志失败", ex);
            }
        }
    }

    private void saveLog(ProceedingJoinPoint point, OperLog operLog, Object result, int status, long costMs) {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) return;
        HttpServletRequest request = attrs.getRequest();

        String username = "anonymous";
        Long userId = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof LoginUser loginUser) {
            username = loginUser.getUsername();
            userId = loginUser.getUserId();
        }
        log.info("[操作日志] user={} module={} operation={} url={} method={} status={} cost={}ms",
                username, operLog.module(), operLog.operation(),
                request.getRequestURI(), request.getMethod(), status, costMs);
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface OperLog {
        String module() default "";
        String operation() default "";
    }
}
