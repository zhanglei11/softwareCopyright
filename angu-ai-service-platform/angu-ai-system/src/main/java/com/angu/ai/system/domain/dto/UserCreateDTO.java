package com.angu.ai.system.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "新增/编辑用户请求")
public class UserCreateDTO {

    @NotBlank @Size(min=2, max=64)
    @Schema(description = "真实姓名")
    private String realName;

    @NotBlank @Size(min=6, max=20) @Pattern(regexp = "^[a-zA-Z0-9]+$")
    @Schema(description = "登录账号")
    private String username;

    @Size(min=8, max=64)
    @Schema(description = "登录密码（新增必填）")
    private String password;

    @NotBlank @Pattern(regexp = "^1[3-9]\\d{9}$")
    @Schema(description = "手机号")
    private String phone;

    @Email
    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "所属部门")
    private String department;

    @Schema(description = "每日调用限额")
    private Integer dailyLimit;

    @NotEmpty
    @Schema(description = "角色 ID 列表")
    private List<Long> roleIds;
}
