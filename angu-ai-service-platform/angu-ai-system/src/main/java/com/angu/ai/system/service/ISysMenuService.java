package com.angu.ai.system.service;

import com.angu.ai.system.domain.dto.MenuDTO;
import com.angu.ai.system.domain.entity.SysMenu;

import java.util.List;

public interface ISysMenuService {
    List<SysMenu> getTree();
    SysMenu getById(Long id);
    void create(MenuDTO dto);
    void update(Long id, MenuDTO dto);
    void deleteById(Long id);
}
