package com.angu.ai.framework.web.exception;

import com.angu.ai.common.constant.HttpStatus;
import com.angu.ai.common.core.domain.AjaxResult;
import com.angu.ai.common.exception.ServiceException;
import com.angu.ai.common.exception.TokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public AjaxResult<Void> handleServiceException(ServiceException e) {
        return AjaxResult.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(TokenException.class)
    public AjaxResult<Void> handleTokenException(TokenException e) {
        return AjaxResult.error(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public AjaxResult<Void> handleAccessDenied(AccessDeniedException e) {
        return AjaxResult.error(HttpStatus.FORBIDDEN, "无操作权限");
    }

    @ExceptionHandler(BadCredentialsException.class)
    public AjaxResult<Void> handleBadCredentials(BadCredentialsException e) {
        return AjaxResult.error(HttpStatus.UNAUTHORIZED, "用户名或密码错误");
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public AjaxResult<Void> handleValidation(Exception e) {
        String msg = e instanceof MethodArgumentNotValidException ve
                ? ve.getBindingResult().getFieldErrors().stream()
                    .map(f -> f.getField() + ": " + f.getDefaultMessage())
                    .findFirst().orElse("参数校验失败")
                : e.getMessage();
        return AjaxResult.error(HttpStatus.BAD_REQUEST, msg);
    }

    @ExceptionHandler(Exception.class)
    public AjaxResult<Void> handleAll(Exception e) {
        log.error("系统异常", e);
        return AjaxResult.error(HttpStatus.ERROR, "系统内部错误");
    }
}
