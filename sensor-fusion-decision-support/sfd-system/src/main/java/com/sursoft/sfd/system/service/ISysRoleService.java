package com.sursoft.sfd.system.service;
import com.sursoft.sfd.system.domain.SysRole;
import java.util.List;
public interface ISysRoleService {
    List<SysRole> list(String keyword, Integer status);
    SysRole getById(Long id);
    void add(SysRole role, Long operatorId);
    void edit(Long id, SysRole role, Long operatorId);
    void delete(Long id);
    void assignMenus(Long id, List<Long> menuIds);
    List<Long> getMenuIds(Long id);
}
