package com.vqcc.system.service;

import com.vqcc.system.domain.SysRole;
import java.util.List;

public interface ISysRoleService {
    List<SysRole> list(String roleName, Integer status);
    SysRole getById(Long id);
    void create(SysRole role, Long operatorId);
    void update(SysRole role, Long operatorId);
    void delete(Long id);
    void assignMenus(Long roleId, List<Long> menuIds);
    List<Long> getMenuIds(Long roleId);
}
