package com.angu.ai.system.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@lombok.NoArgsConstructor
@Schema(description = "登录响应")
public class LoginVO {
    @Schema(description = "访问令牌")
    private String accessToken;
    @Schema(description = "刷新令牌")
    private String refreshToken;
    @Schema(description = "令牌类型")
    private String tokenType = "Bearer";
    @Schema(description = "过期时间（秒）")
    private Long expiresIn;
    @Schema(description = "用户信息")
    private UserVO userInfo;
    @Schema(description = "权限列表")
    private Set<String> permissions;
    @Schema(description = "角色列表")
    private List<String> roles;
}
