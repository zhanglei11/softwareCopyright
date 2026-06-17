package com.sursoft.iidp.system.sys.domain;

import com.sursoft.iidp.common.core.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "菜单/权限")
public class SysMenu extends BaseEntity {
    private Long id;
    private Long parentId;
    private String menuName;
    private String menuType;
    private String path;
    private String component;
    private String perms;
    private String icon;
    private Integer sortOrder;
    private Integer visible;
    private Integer status;
    private List<SysMenu> children;
}
