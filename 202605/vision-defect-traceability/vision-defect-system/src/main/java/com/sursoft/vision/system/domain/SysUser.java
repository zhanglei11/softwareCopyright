package com.sursoft.vision.system.domain;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SysUser {
    private Long id;
    private String username;
    private String realName;
    private String phone;
    private String department;
    private String password;
    private Integer status;
    private Integer isDeleted;
    private LocalDateTime createdAt;
    private Long createdBy;
    private LocalDateTime updatedAt;
    private Long updatedBy;
}
