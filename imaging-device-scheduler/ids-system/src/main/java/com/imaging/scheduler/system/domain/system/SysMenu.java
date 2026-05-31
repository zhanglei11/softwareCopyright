package com.imaging.scheduler.system.domain.system;

import lombok.Data;

@Data
public class SysMenu {
    private Long id;
    private Long parentId;
    private String menuName;
    private Integer menuType;
    private String path;
    private String permission;
    private String icon;
    private Integer sort;
    private Integer status;
    private Integer isDeleted;
}
