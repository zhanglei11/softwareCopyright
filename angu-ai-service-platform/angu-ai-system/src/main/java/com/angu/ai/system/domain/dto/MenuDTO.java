package com.angu.ai.system.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "菜单请求")
public class MenuDTO {

    @Schema(description = "父级 ID，0 表示顶级")
    private Long parentId = 0L;

    @NotBlank
    @Schema(description = "菜单名称")
    private String menuName;

    @NotBlank
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
    private Integer sort = 0;

    @Schema(description = "是否可见：0 隐藏 / 1 显示")
    private Integer visible = 1;
}
