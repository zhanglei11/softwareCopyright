package com.imaging.scheduler.system.domain.system;

import com.imaging.scheduler.common.core.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SysRole extends BaseEntity {
    private Long id;
    private String roleName;
    private String roleCode;
    private String description;
    private Integer status;
    private Integer isDeleted;
}
