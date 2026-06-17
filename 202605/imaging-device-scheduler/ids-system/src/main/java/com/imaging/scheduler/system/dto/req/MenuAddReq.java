package com.imaging.scheduler.system.dto.req;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class MenuAddReq {
    @NotNull
    private Long parentId;
    @NotBlank @Size(max=30)
    private String menuName;
    @NotNull
    private Integer menuType;
    private String path;
    private String permission;
    private String icon;
    @NotNull
    private Integer sort;
    @NotNull
    private Integer status;
}
