package com.angu.matcher.system.service.impl;

import com.angu.matcher.common.exception.ServiceException;
import com.angu.matcher.system.domain.SysMenu;
import com.angu.matcher.system.mapper.SysMenuMapper;
import com.angu.matcher.system.service.ISysMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SysMenuServiceImpl implements ISysMenuService {

    private final SysMenuMapper menuMapper;

    @Override
    public List<SysMenu> getMenuTree() {
        return buildTree(menuMapper.selectAll());
    }

    @Override
    public List<SysMenu> getMenuTreeByUserId(Long userId) {
        return buildTree(menuMapper.selectMenusByUserId(userId));
    }

    private List<SysMenu> buildTree(List<SysMenu> all) {
        List<SysMenu> roots = new ArrayList<>();
        for (SysMenu m : all) {
            if (m.getParentId() == null || m.getParentId() == 0) {
                m.setChildren(getChildren(m.getId(), all));
                roots.add(m);
            }
        }
        return roots;
    }

    private List<SysMenu> getChildren(Long parentId, List<SysMenu> all) {
        List<SysMenu> children = new ArrayList<>();
        for (SysMenu m : all) {
            if (parentId.equals(m.getParentId())) {
                m.setChildren(getChildren(m.getId(), all));
                children.add(m);
            }
        }
        return children;
    }

    @Override
    public void createMenu(SysMenu menu) {
        menuMapper.insert(menu);
    }

    @Override
    public void updateMenu(SysMenu menu) {
        if (menuMapper.selectById(menu.getId()) == null) throw new ServiceException(404, "菜单不存在");
        menuMapper.update(menu);
    }

    @Override
    public void deleteMenu(Long id) {
        if (menuMapper.countChildrenByParentId(id) > 0) {
            throw new ServiceException(400, "请先删除子菜单");
        }
        menuMapper.deleteById(id);
    }
}
