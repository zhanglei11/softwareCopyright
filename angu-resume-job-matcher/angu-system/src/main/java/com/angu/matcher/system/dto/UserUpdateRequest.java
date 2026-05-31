package com.angu.matcher.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "编辑用户请求")
public class UserUpdateRequest {
    private String realName;
    private String phone;
    private Integer status;
    private List<Long> roleIds;
}
