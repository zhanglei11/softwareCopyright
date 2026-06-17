package com.sursoft.iidp.common.core;

import com.sursoft.iidp.common.constant.HttpStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.io.Serial;
import java.io.Serializable;

@Data
@Schema(description = "通用响应体")
public class AjaxResult<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "状态码 200=成功")
    private int code;

    @Schema(description = "提示消息")
    private String msg;

    @Schema(description = "数据")
    private T data;

    public static <T> AjaxResult<T> success() {
        return success(null);
    }

    public static <T> AjaxResult<T> success(T data) {
        AjaxResult<T> r = new AjaxResult<>();
        r.code = HttpStatus.SUCCESS;
        r.msg = "操作成功";
        r.data = data;
        return r;
    }

    public static <T> AjaxResult<T> error(String msg) {
        AjaxResult<T> r = new AjaxResult<>();
        r.code = HttpStatus.ERROR;
        r.msg = msg;
        return r;
    }

    public static <T> AjaxResult<T> error(int code, String msg) {
        AjaxResult<T> r = new AjaxResult<>();
        r.code = code;
        r.msg = msg;
        return r;
    }
}
