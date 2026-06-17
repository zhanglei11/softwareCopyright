package com.vqcc.common.result;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AjaxResult<T> implements Serializable {

    private int code;
    private String msg;
    private T data;

    private AjaxResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> AjaxResult<T> ok() {
        return new AjaxResult<>(200, "操作成功", null);
    }

    public static <T> AjaxResult<T> ok(T data) {
        return new AjaxResult<>(200, "操作成功", data);
    }

    public static <T> AjaxResult<T> ok(String msg, T data) {
        return new AjaxResult<>(200, msg, data);
    }

    public static <T> AjaxResult<T> fail(String msg) {
        return new AjaxResult<>(500, msg, null);
    }

    public static <T> AjaxResult<T> fail(int code, String msg) {
        return new AjaxResult<>(code, msg, null);
    }

    public int getCode() { return code; }
    public String getMsg() { return msg; }
    public T getData() { return data; }
}
