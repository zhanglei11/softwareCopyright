package com.imaging.scheduler.system.service.system;

import com.imaging.scheduler.system.domain.system.SysMenu;
import com.imaging.scheduler.system.dto.req.MenuAddReq;

import java.util.List;

public interface SysMenuService {
    List<SysMenu> getMenuTree();
    void addMenu(MenuAddReq req);
    void editMenu(Long id, MenuAddReq req);
    void deleteMenu(Long id);
}
