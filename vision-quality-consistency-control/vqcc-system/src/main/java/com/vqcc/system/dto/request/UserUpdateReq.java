package com.vqcc.system.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "更新用户请求")
public class UserUpdateReq {
    @NotNull(message = "用户ID不能为空")
    private Long id;
    private String realName;
    private String phone;
    private String dept;
    private Integer status;
}
