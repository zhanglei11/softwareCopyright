package com.sursoft.iidp.system.sys.service;

import com.sursoft.iidp.system.sys.domain.SysRole;
import java.util.List;

public interface SysRoleService {
    List<SysRole> listRoles(SysRole query);
    SysRole getRoleById(Long id);
    int addRole(SysRole role);
    int editRole(SysRole role);
    int removeRole(Long id);
}
