package com.angu.ai.system.domain.entity;

import com.angu.ai.common.core.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "系统用户")
public class SysUser extends BaseEntity {

    @Schema(description = "登录账号")
    private String username;

    @Schema(description = "真实姓名")
    private String realName;

    @Schema(description = "手机号码")
    private String phone;

    @Schema(description = "邮箱地址")
    private String email;

    @Schema(description = "所属部门")
    private String department;

    @Schema(description = "登录密码（BCrypt）")
    private String password;

    @Schema(description = "每日调用限额，NULL 不限制")
    private Integer dailyLimit;

    @Schema(description = "状态：0 禁用 / 1 启用")
    private Integer status;

    @Schema(description = "连续密码错误次数")
    private Integer errorCount;

    @Schema(description = "锁定截止时间")
    private LocalDateTime lockedUntil;

    @Schema(description = "是否删除：0 正常 / 1 已删除")
    private Integer deleted;
}
