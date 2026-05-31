package com.sursoft.sfd.system.domain;

import com.sursoft.sfd.common.core.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter @Setter
@Schema(description = "菜单")
public class SysMenu extends BaseEntity {
    @Schema(description = "父节点ID") private Long parentId;
    @Schema(description = "菜单名称") private String menuName;
    @Schema(description = "类型 M目录 C菜单 F按钮") private String menuType;
    @Schema(description = "路由路径") private String path;
    @Schema(description = "组件路径") private String component;
    @Schema(description = "图标")     private String icon;
    @Schema(description = "权限标识") private String permission;
    @Schema(description = "排序")     private Integer sort;
    @Schema(description = "子菜单")   private List<SysMenu> children;
}
