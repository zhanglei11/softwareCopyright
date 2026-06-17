package com.sursoft.iidp.system.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "登录响应")
public class LoginResponse {
    @Schema(description = "访问Token(2h有效)")
    private String accessToken;

    @Schema(description = "刷新Token(7d有效)")
    private String refreshToken;

    @Schema(description = "Token类型")
    private String tokenType = "Bearer";
}
