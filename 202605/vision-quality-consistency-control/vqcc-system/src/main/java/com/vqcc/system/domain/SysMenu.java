package com.vqcc.system.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.List;

@Data
@Schema(description = "菜单")
public class SysMenu {
    private Long id;
    private Long parentId;
    @Schema(description = "菜单名称")
    private String menuName;
    @Schema(description = "菜单类型 0-目录 1-菜单 2-按钮")
    private Integer menuType;
    private String path;
    private String permission;
    private String icon;
    private Integer sort;
    private Integer status;
    private List<SysMenu> children;
}
