package com.sursoft.iidp.system.sys.domain;

import com.sursoft.iidp.common.core.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "系统用户")
public class SysUser extends BaseEntity {
    @Schema(description = "用户ID")
    private Long id;
    @Schema(description = "用户名")
    private String username;
    @Schema(description = "密码(BCrypt)")
    private String password;
    @Schema(description = "真实姓名")
    private String realName;
    @Schema(description = "邮箱")
    private String email;
    @Schema(description = "手机号")
    private String phone;
    @Schema(description = "头像URL")
    private String avatar;
    @Schema(description = "状态 0停用 1启用")
    private Integer status;
    @Schema(description = "备注")
    private String remark;
}
