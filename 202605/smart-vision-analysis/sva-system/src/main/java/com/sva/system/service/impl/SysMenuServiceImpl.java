package com.sva.system.service.impl;

import com.sva.common.exception.ServiceException;
import com.sva.system.domain.SysMenu;
import com.sva.system.mapper.SysMenuMapper;
import com.sva.system.service.ISysMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SysMenuServiceImpl implements ISysMenuService {

    private final SysMenuMapper menuMapper;

    @Override
    public List<SysMenu> allTree() {
        return buildTree(menuMapper.selectAll(), 0L);
    }

    @Override
    public List<SysMenu> treeByUserId(Long userId) {
        return allTree();
    }

    @Override
    public SysMenu getById(Long id) {
        SysMenu menu = menuMapper.selectById(id);
        if (menu == null) throw new ServiceException(404, "菜单不存在");
        return menu;
    }

    @Override
    public void add(SysMenu menu) {
        if (menu.getParentId() == null) menu.setParentId(0L);
        if (menu.getStatus() == null) menu.setStatus(1);
        if (menu.getVisible() == null) menu.setVisible(1);
        if (menu.getSortOrder() == null) menu.setSortOrder(0);
        menuMapper.insert(menu);
    }

    @Override
    public void update(SysMenu menu) {
        getById(menu.getId());
        menuMapper.update(menu);
    }

    @Override
    public void deleteById(Long id) {
        if (menuMapper.countChildrenByParentId(id) > 0) throw new ServiceException(400, "存在子菜单，无法删除");
        menuMapper.deleteById(id);
    }

    private List<SysMenu> buildTree(List<SysMenu> all, Long parentId) {
        List<SysMenu> result = new ArrayList<>();
        for (SysMenu m : all) {
            if (parentId.equals(m.getParentId())) {
                m.setChildren(buildTree(all, m.getId()));
                result.add(m);
            }
        }
        return result;
    }
}
