package com.angu.ai.system.domain.entity;

import com.angu.ai.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "菜单权限")
public class SysMenu extends BaseEntity {

    @Schema(description = "菜单名称")
    private String menuName;

    @Schema(description = "父级 ID，0 表示顶级")
    private Long parentId;

    @Schema(description = "类型：D 目录 / M 菜单 / B 按钮")
    private String menuType;

    @Schema(description = "路由地址")
    private String path;

    @Schema(description = "组件路径")
    private String component;

    @Schema(description = "权限标识")
    private String perms;

    @Schema(description = "图标")
    private String icon;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "是否可见：0 隐藏 / 1 显示")
    private Integer visible;

    @Schema(description = "状态：0 禁用 / 1 启用")
    private Integer status;

    @Schema(description = "子菜单")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<SysMenu> children;
}
