package com.angu.matcher.system.domain;

import com.angu.matcher.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "系统角色")
public class SysRole extends BaseEntity {
    private Long id;
    private String roleName;
    private String roleCode;
    private Integer builtin;
    private Integer status;
    private String remark;
}
