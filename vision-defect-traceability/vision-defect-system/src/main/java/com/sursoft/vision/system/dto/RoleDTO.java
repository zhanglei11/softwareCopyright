package com.sursoft.vision.system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RoleDTO {
    @NotBlank @Size(max=50)
    private String roleName;
    @NotBlank @Size(max=50)
    private String roleKey;
    @Size(max=500)
    private String remark;
    private Integer status;
}
