package com.sursoft.sfd.system.service;
import com.sursoft.sfd.system.domain.SysMenu;
import java.util.List;
public interface ISysMenuService {
    List<SysMenu> tree();
    SysMenu getById(Long id);
    void add(SysMenu menu, Long operatorId);
    void edit(Long id, SysMenu menu, Long operatorId);
    void delete(Long id);
}
