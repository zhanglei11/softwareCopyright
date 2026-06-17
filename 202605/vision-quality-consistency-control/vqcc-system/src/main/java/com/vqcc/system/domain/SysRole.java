package com.vqcc.system.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "系统角色")
public class SysRole {
    private Long id;
    @Schema(description = "角色名称")
    private String roleName;
    @Schema(description = "角色标识")
    private String roleCode;
    private String description;
    private Integer status;
    private Integer isDeleted;
    private LocalDateTime createdAt;
    private Long createdBy;
    private LocalDateTime updatedAt;
    private Long updatedBy;
}
