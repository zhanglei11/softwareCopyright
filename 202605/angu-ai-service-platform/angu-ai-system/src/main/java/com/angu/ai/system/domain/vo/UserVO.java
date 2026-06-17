package com.angu.ai.system.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description = "用户详情 VO")
public class UserVO {
    @Schema(description = "用户 ID")
    private Long id;
    @Schema(description = "登录账号")
    private String username;
    @Schema(description = "真实姓名")
    private String realName;
    @Schema(description = "手机号")
    private String phone;
    @Schema(description = "邮箱")
    private String email;
    @Schema(description = "所属部门")
    private String department;
    @Schema(description = "每日限额")
    private Integer dailyLimit;
    @Schema(description = "状态")
    private Integer status;
    @Schema(description = "角色列表")
    private List<RoleVO> roles;
    @Schema(description = "创建时间")
    private LocalDateTime createdTime;
}
