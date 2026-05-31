package com.imaging.scheduler.system.dto.req;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RoleAddReq {
    @NotBlank @Size(max=30)
    private String roleName;
    @NotBlank @Pattern(regexp = "^[A-Z_]+$", message = "角色标识只能包含大写字母和下划线")
    private String roleCode;
    @Size(max=200)
    private String description;
    @NotNull
    private Integer status;
}
