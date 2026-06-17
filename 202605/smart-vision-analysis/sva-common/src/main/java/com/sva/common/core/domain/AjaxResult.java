package com.sva.common.core.domain;

import java.io.Serializable;

public class AjaxResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final int SUCCESS = 200;
    public static final int FAIL = 500;

    private int code;
    private String message;
    private T data;

    public AjaxResult() {}

    public AjaxResult(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> AjaxResult<T> success() {
        return new AjaxResult<>(SUCCESS, "操作成功", null);
    }

    public static <T> AjaxResult<T> success(T data) {
        return new AjaxResult<>(SUCCESS, "操作成功", data);
    }

    public static <T> AjaxResult<T> success(String message, T data) {
        return new AjaxResult<>(SUCCESS, message, data);
    }

    public static <T> AjaxResult<T> error() {
        return new AjaxResult<>(FAIL, "操作失败", null);
    }

    public static <T> AjaxResult<T> error(String message) {
        return new AjaxResult<>(FAIL, message, null);
    }

    public static <T> AjaxResult<T> error(int code, String message) {
        return new AjaxResult<>(code, message, null);
    }

    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
}
