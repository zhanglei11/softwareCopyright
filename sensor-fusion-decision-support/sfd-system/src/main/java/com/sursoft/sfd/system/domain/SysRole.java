package com.sursoft.sfd.system.domain;

import com.sursoft.sfd.common.core.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Schema(description = "系统角色")
public class SysRole extends BaseEntity {
    @Schema(description = "角色名称") private String roleName;
    @Schema(description = "角色标识") private String roleCode;
    @Schema(description = "描述")     private String description;
}
