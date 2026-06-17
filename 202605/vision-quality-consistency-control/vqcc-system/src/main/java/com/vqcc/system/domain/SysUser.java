package com.vqcc.system.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "系统用户")
public class SysUser {
    @Schema(description = "用户ID")
    private Long id;
    @Schema(description = "登录账号")
    private String username;
    @JsonIgnore
    private String password;
    @Schema(description = "真实姓名")
    private String realName;
    @Schema(description = "手机号")
    private String phone;
    @Schema(description = "部门")
    private String dept;
    @Schema(description = "状态 0-禁用 1-启用")
    private Integer status;
    private Integer isDeleted;
    private LocalDateTime createdAt;
    private Long createdBy;
    private LocalDateTime updatedAt;
    private Long updatedBy;
}
