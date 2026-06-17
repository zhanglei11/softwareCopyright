package com.angu.matcher.system.service;

import com.angu.matcher.system.domain.SysRole;

import java.util.List;

public interface ISysRoleService {
    List<SysRole> listRoles();
    SysRole getById(Long id);
    void createRole(SysRole role);
    void updateRole(SysRole role);
    void deleteRole(Long id);
    void assignMenus(Long roleId, List<Long> menuIds);
    List<Long> getMenuIdsByRoleId(Long roleId);
    List<String> getPermCodesByUserId(Long userId);
}
