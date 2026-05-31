package com.imaging.scheduler.system.domain.system;

import com.imaging.scheduler.common.core.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SysUser extends BaseEntity {
    private Long id;
    private String username;
    private String realName;
    private String password;
    private String phone;
    private String department;
    private Integer status;
    private Integer isDeleted;
}
