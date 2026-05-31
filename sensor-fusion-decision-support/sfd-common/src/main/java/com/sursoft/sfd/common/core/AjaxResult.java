package com.sursoft.sfd.common.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "统一响应体")
public class AjaxResult<T> implements Serializable {

    @Schema(description = "状态码")
    private int code;
    @Schema(description = "提示信息")
    private String message;
    @Schema(description = "业务数据")
    private T data;

    private AjaxResult() {}

    public static <T> AjaxResult<T> ok() {
        return ok(null);
    }

    public static <T> AjaxResult<T> ok(T data) {
        AjaxResult<T> r = new AjaxResult<>();
        r.code = 200;
        r.message = "ok";
        r.data = data;
        return r;
    }

    public static <T> AjaxResult<T> fail(int code, String message) {
        AjaxResult<T> r = new AjaxResult<>();
        r.code = code;
        r.message = message;
        return r;
    }

    public static <T> AjaxResult<T> fail(String message) {
        return fail(500, message);
    }

    public int getCode() { return code; }
    public String getMessage() { return message; }
    public T getData() { return data; }
}
