package com.imaging.scheduler.system.service.system;

import com.imaging.scheduler.system.domain.system.SysRole;
import com.imaging.scheduler.system.dto.req.*;

import java.util.List;

public interface SysRoleService {
    List<SysRole> getRoleList();
    void addRole(RoleAddReq req);
    void editRole(Long id, RoleAddReq req);
    void assignMenus(Long id, RoleMenuReq req);
    void deleteRole(Long id);
}
