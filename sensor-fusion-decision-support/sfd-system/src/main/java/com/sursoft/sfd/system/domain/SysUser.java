package com.sursoft.sfd.system.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sursoft.sfd.common.core.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter @Setter
@Schema(description = "系统用户")
public class SysUser extends BaseEntity {
    @Schema(description = "登录账号") private String username;
    @Schema(description = "姓名")     private String realName;
    @JsonIgnore
    @Schema(hidden = true)             private String password;
    @Schema(description = "手机号")   private String phone;
    @Schema(description = "部门")     private String department;
    @Schema(description = "角色列表") private List<SysRole> roles;
}
