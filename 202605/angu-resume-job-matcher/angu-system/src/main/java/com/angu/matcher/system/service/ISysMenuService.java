package com.angu.matcher.system.service;

import com.angu.matcher.system.domain.SysMenu;

import java.util.List;

public interface ISysMenuService {
    List<SysMenu> getMenuTree();
    List<SysMenu> getMenuTreeByUserId(Long userId);
    void createMenu(SysMenu menu);
    void updateMenu(SysMenu menu);
    void deleteMenu(Long id);
}
