package com.angu.ai.system.service.impl;

import com.angu.ai.common.exception.ServiceException;
import com.angu.ai.system.domain.dto.MenuDTO;
import com.angu.ai.system.domain.entity.SysMenu;
import com.angu.ai.system.mapper.SysMenuMapper;
import com.angu.ai.system.service.ISysMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SysMenuServiceImpl implements ISysMenuService {
    private final SysMenuMapper menuMapper;

    @Override
    public List<SysMenu> getTree() { return buildTree(menuMapper.selectAll(), 0L); }

    private List<SysMenu> buildTree(List<SysMenu> all, Long parentId) {
        List<SysMenu> result = new ArrayList<>();
        for (SysMenu m : all) {
            if (parentId.equals(m.getParentId())) {
                m.setChildren(buildTree(all, m.getId())); result.add(m);
            }
        }
        return result;
    }

    @Override
    public SysMenu getById(Long id) {
        SysMenu m = menuMapper.selectById(id);
        if (m == null) throw new ServiceException(404, "菜单不存在");
        return m;
    }

    @Override
    public void create(MenuDTO dto) {
        SysMenu m = new SysMenu();
        m.setMenuName(dto.getMenuName()); m.setParentId(dto.getParentId());
        m.setMenuType(dto.getMenuType()); m.setPath(dto.getPath());
        m.setComponent(dto.getComponent()); m.setPerms(dto.getPerms());
        m.setIcon(dto.getIcon()); m.setSort(dto.getSort()); m.setVisible(dto.getVisible());
        menuMapper.insert(m);
    }

    @Override
    public void update(Long id, MenuDTO dto) {
        SysMenu m = getById(id);
        m.setMenuName(dto.getMenuName()); m.setPath(dto.getPath());
        m.setComponent(dto.getComponent()); m.setPerms(dto.getPerms());
        m.setIcon(dto.getIcon()); m.setSort(dto.getSort()); m.setVisible(dto.getVisible());
        menuMapper.updateById(m);
    }

    @Override
    public void deleteById(Long id) {
        if (menuMapper.countChildren(id) > 0) throw new ServiceException(400, "存在子菜单，无法删除");
        menuMapper.deleteById(id);
    }
}
