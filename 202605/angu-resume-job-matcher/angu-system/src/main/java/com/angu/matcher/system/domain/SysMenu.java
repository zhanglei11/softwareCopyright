package com.angu.matcher.system.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "系统菜单")
public class SysMenu {
    private Long id;
    private Long parentId;
    private Integer menuType;
    private String menuName;
    private String path;
    private String permCode;
    private String icon;
    private Integer sort;
    private List<SysMenu> children;
}
