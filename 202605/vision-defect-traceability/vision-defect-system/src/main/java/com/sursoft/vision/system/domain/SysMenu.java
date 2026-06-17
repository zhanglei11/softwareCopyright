package com.sursoft.vision.system.domain;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class SysMenu {
    private Long id;
    private Long parentId;
    private String menuName;
    private Integer menuType;
    private String path;
    private String perms;
    private Integer orderNum;
    private String icon;
    private Integer isVisible;
    private LocalDateTime createdAt;
    private List<SysMenu> children;
}
