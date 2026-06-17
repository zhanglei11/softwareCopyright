package com.angu.matcher.system.dto;

import lombok.Data;

import java.util.List;

@Data
public class RoleMenuRequest {
    private List<Long> menuIds;
}
