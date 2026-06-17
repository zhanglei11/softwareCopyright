package com.sursoft.vision.system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MenuDTO {
    private Long parentId;
    @NotBlank @jakarta.validation.constraints.Size(max=50)
    private String menuName;
    @NotNull
    private Integer menuType;
    private String path;
    private String perms;
    private Integer orderNum;
    private String icon;
    private Integer isVisible;
}
