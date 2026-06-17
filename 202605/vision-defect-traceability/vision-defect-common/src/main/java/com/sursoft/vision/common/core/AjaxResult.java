package com.sursoft.vision.common.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AjaxResult<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Integer code;
    private String msg;
    private T data;

    private AjaxResult(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> AjaxResult<T> success() {
        return new AjaxResult<>(200, "操作成功", null);
    }

    public static <T> AjaxResult<T> success(T data) {
        return new AjaxResult<>(200, "操作成功", data);
    }

    public static <T> AjaxResult<T> success(String msg, T data) {
        return new AjaxResult<>(200, msg, data);
    }

    public static <T> AjaxResult<T> error(String msg) {
        return new AjaxResult<>(500, msg, null);
    }

    public static <T> AjaxResult<T> error(Integer code, String msg) {
        return new AjaxResult<>(code, msg, null);
    }
}
