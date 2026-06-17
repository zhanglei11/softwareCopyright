package com.angu.matcher.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "新增用户请求")
public class UserCreateRequest {
    @NotBlank(message = "用户名不能为空")
    private String username;
    @NotBlank(message = "真实姓名不能为空")
    private String realName;
    @NotBlank(message = "手机号不能为空")
    private String phone;
    @NotBlank(message = "初始密码不能为空")
    private String password;
    private List<Long> roleIds;
}
