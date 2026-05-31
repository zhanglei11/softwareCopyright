package com.vqcc.system.service;

import com.vqcc.system.domain.SysMenu;
import java.util.List;

public interface ISysMenuService {
    List<SysMenu> tree();
    List<SysMenu> treeByUserId(Long userId);
    void create(SysMenu menu);
    void update(SysMenu menu);
    void delete(Long id);
}
