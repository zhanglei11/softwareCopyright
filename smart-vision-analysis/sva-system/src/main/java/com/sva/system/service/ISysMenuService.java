package com.sva.system.service;

import com.sva.system.domain.SysMenu;
import java.util.List;

public interface ISysMenuService {
    List<SysMenu> allTree();
    List<SysMenu> treeByUserId(Long userId);
    SysMenu getById(Long id);
    void add(SysMenu menu);
    void update(SysMenu menu);
    void deleteById(Long id);
}
