package com.angu.matcher.system.domain;

import com.angu.matcher.common.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "系统用户")
public class SysUser extends BaseEntity {
    private Long id;
    private String username;
    private String realName;
    private String phone;
    @JsonIgnore
    private String password;
    private Integer status;
    private Integer errorCount;
    private LocalDateTime lockedUntil;
    private Integer deleted;
}
