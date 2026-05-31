package com.angu.ai.common.core.domain;

import com.angu.ai.common.constant.HttpStatus;
import com.alibaba.fastjson2.annotation.JSONField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "统一响应结果")
public class AjaxResult<T> implements Serializable {

    @Schema(description = "响应码，200 表示成功")
    private int code;

    @Schema(description = "响应消息")
    private String message;

    @Schema(description = "响应数据")
    private T data;

    private AjaxResult() {}

    public static <T> AjaxResult<T> success() {
        return success(null);
    }

    public static <T> AjaxResult<T> success(T data) {
        return success("success", data);
    }

    public static <T> AjaxResult<T> success(String message, T data) {
        AjaxResult<T> r = new AjaxResult<>();
        r.code = HttpStatus.SUCCESS;
        r.message = message;
        r.data = data;
        return r;
    }

    public static <T> AjaxResult<T> error(String message) {
        return error(HttpStatus.ERROR, message);
    }

    public static <T> AjaxResult<T> error(int code, String message) {
        AjaxResult<T> r = new AjaxResult<>();
        r.code = code;
        r.message = message;
        return r;
    }

    @JSONField(serialize = false)
    public boolean isSuccess() {
        return this.code == HttpStatus.SUCCESS;
    }
}
