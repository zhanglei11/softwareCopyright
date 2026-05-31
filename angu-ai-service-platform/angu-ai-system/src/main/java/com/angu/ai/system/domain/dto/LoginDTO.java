package com.angu.ai.system.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "登录请求")
public class LoginDTO {

    @NotBlank
    @Schema(description = "登录账号")
    private String username;

    @NotBlank
    @Schema(description = "登录密码")
    private String password;

    @Schema(description = "图形验证码 Key")
    private String captchaKey;

    @Schema(description = "图形验证码")
    private String captchaCode;
}
