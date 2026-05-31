package com.sursoft.iidp.system.sys.service.impl;

import com.sursoft.iidp.system.sys.domain.SysMenu;
import com.sursoft.iidp.system.sys.mapper.SysMenuMapper;
import com.sursoft.iidp.system.sys.service.SysMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SysMenuServiceImpl implements SysMenuService {
    private final SysMenuMapper menuMapper;

    @Override
    public List<SysMenu> listMenus() {
        return buildTree(menuMapper.selectAllMenus(), 0L);
    }

    @Override
    public List<SysMenu> getMenuTreeByUserId(Long userId) {
        return buildTree(menuMapper.selectMenuTreeByUserId(userId), 0L);
    }

    @Override public SysMenu getMenuById(Long id) { return menuMapper.selectById(id); }
    @Override public int addMenu(SysMenu menu) { return menuMapper.insert(menu); }
    @Override public int editMenu(SysMenu menu) { return menuMapper.update(menu); }
    @Override public int removeMenu(Long id) { return menuMapper.deleteById(id); }

    private List<SysMenu> buildTree(List<SysMenu> all, Long parentId) {
        List<SysMenu> result = new ArrayList<>();
        for (SysMenu menu : all) {
            if (parentId.equals(menu.getParentId())) {
                menu.setChildren(buildTree(all, menu.getId()));
                result.add(menu);
            }
        }
        return result;
    }
}
