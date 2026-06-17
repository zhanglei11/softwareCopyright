package com.sursoft.iidp.system.sys.service;

import com.sursoft.iidp.system.sys.domain.SysMenu;
import java.util.List;

public interface SysMenuService {
    List<SysMenu> listMenus();
    List<SysMenu> getMenuTreeByUserId(Long userId);
    SysMenu getMenuById(Long id);
    int addMenu(SysMenu menu);
    int editMenu(SysMenu menu);
    int removeMenu(Long id);
}
