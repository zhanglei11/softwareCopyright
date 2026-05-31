package com.sva.system.service;

import com.sva.system.domain.SysRole;
import java.util.List;

public interface ISysRoleService {
    List<SysRole> list(String roleName, Integer status);
    SysRole getById(Long id);
    void add(SysRole role, Long operatorId);
    void update(SysRole role, Long operatorId);
    void deleteById(Long id);
    void assignMenus(Long roleId, List<Long> menuIds);
    List<Long> getMenuIds(Long roleId);
}
