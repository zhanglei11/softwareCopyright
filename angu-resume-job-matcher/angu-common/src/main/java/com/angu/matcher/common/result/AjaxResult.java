package com.angu.matcher.common.result;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "统一响应体")
public class AjaxResult<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "状态码")
    private int code;
    @Schema(description = "消息")
    private String message;
    @Schema(description = "数据")
    private T data;

    private AjaxResult() {}

    public static <T> AjaxResult<T> success() {
        return success(null);
    }

    public static <T> AjaxResult<T> success(T data) {
        AjaxResult<T> r = new AjaxResult<>();
        r.code = 200;
        r.message = "success";
        r.data = data;
        return r;
    }

    public static <T> AjaxResult<T> error(String message) {
        return error(500, message);
    }

    public static <T> AjaxResult<T> error(int code, String message) {
        AjaxResult<T> r = new AjaxResult<>();
        r.code = code;
        r.message = message;
        return r;
    }
}
