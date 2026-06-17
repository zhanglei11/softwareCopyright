package com.sursoft.vision.system.service;

import com.sursoft.vision.system.domain.SysMenu;
import com.sursoft.vision.system.dto.MenuDTO;
import java.util.List;

public interface SysMenuService {
    List<SysMenu> tree();
    void add(MenuDTO dto);
    void edit(Long id, MenuDTO dto);
    void delete(Long id);
}
