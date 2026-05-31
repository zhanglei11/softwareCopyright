package com.imaging.scheduler.common.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

/**
 * 统一响应结果
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AjaxResult<T> implements Serializable {

    private Integer code;
    private String message;
    private T data;

    private AjaxResult(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> AjaxResult<T> success() {
        return new AjaxResult<>(200, "ok", null);
    }

    public static <T> AjaxResult<T> success(T data) {
        return new AjaxResult<>(200, "ok", data);
    }

    public static <T> AjaxResult<T> success(String message, T data) {
        return new AjaxResult<>(200, message, data);
    }

    public static <T> AjaxResult<T> error(String message) {
        return new AjaxResult<>(500, message, null);
    }

    public static <T> AjaxResult<T> error(Integer code, String message) {
        return new AjaxResult<>(code, message, null);
    }
}
