package com.angu.ai.system.domain.entity;

import com.angu.ai.common.core.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "系统角色")
public class SysRole extends BaseEntity {

    @Schema(description = "角色名称")
    private String roleName;

    @Schema(description = "角色标识")
    private String roleCode;

    @Schema(description = "是否内置：1 内置不可删除")
    private Integer builtin;

    @Schema(description = "状态：0 禁用 / 1 启用")
    private Integer status;

    @Schema(description = "描述")
    private String description;
}
