package com.angu.ai.system.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "角色 VO")
public class RoleVO {
    @Schema(description = "角色 ID")
    private Long id;
    @Schema(description = "角色名称")
    private String roleName;
    @Schema(description = "角色标识")
    private String roleCode;
    @Schema(description = "状态")
    private Integer status;
    @Schema(description = "描述")
    private String description;
}
