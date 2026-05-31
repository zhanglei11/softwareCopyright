package com.sursoft.sfd.framework.web;

import com.sursoft.sfd.common.core.AjaxResult;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SecurityExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public AjaxResult<Void> handleAccessDeniedException(AccessDeniedException e) {
        return AjaxResult.fail(403, "无权限访问");
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public AjaxResult<Void> handleBadCredentialsException(BadCredentialsException e) {
        return AjaxResult.fail(401, "账号或密码错误");
    }
}
