package com.angu.ai.system.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "角色请求")
public class RoleDTO {

    @NotBlank
    @Schema(description = "角色名称")
    private String roleName;

    @NotBlank
    @Schema(description = "角色标识")
    private String roleCode;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "菜单 ID 列表")
    private List<Long> menuIds;

    @Schema(description = "场景 ID 列表")
    private List<Long> sceneIds;
}
