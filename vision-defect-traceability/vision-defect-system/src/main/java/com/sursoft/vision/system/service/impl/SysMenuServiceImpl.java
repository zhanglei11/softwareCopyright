package com.sursoft.vision.system.service.impl;

import com.sursoft.vision.system.domain.SysMenu;
import com.sursoft.vision.system.dto.MenuDTO;
import com.sursoft.vision.system.mapper.SysMenuMapper;
import com.sursoft.vision.system.service.SysMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SysMenuServiceImpl implements SysMenuService {

    private final SysMenuMapper menuMapper;

    @Override
    public List<SysMenu> tree() {
        List<SysMenu> all = menuMapper.selectAll();
        return buildTree(all, 0L);
    }

    private List<SysMenu> buildTree(List<SysMenu> all, Long parentId) {
        List<SysMenu> result = new ArrayList<>();
        for (SysMenu menu : all) {
            Long pid = menu.getParentId() == null ? 0L : menu.getParentId();
            if (pid.equals(parentId)) {
                menu.setChildren(buildTree(all, menu.getId()));
                result.add(menu);
            }
        }
        return result;
    }

    @Override
    public void add(MenuDTO dto) {
        SysMenu menu = new SysMenu();
        menu.setParentId(dto.getParentId() != null ? dto.getParentId() : 0L);
        menu.setMenuName(dto.getMenuName());
        menu.setMenuType(dto.getMenuType());
        menu.setPath(dto.getPath());
        menu.setPerms(dto.getPerms());
        menu.setOrderNum(dto.getOrderNum() != null ? dto.getOrderNum() : 0);
        menu.setIcon(dto.getIcon());
        menu.setIsVisible(dto.getIsVisible() != null ? dto.getIsVisible() : 1);
        menuMapper.insert(menu);
    }

    @Override
    public void edit(Long id, MenuDTO dto) {
        SysMenu menu = menuMapper.selectById(id);
        if (menu == null) throw new com.sursoft.vision.common.exception.ServiceException("菜单不存在");
        menu.setMenuName(dto.getMenuName());
        menu.setMenuType(dto.getMenuType());
        menu.setPath(dto.getPath());
        menu.setPerms(dto.getPerms());
        menu.setOrderNum(dto.getOrderNum() != null ? dto.getOrderNum() : menu.getOrderNum());
        menu.setIcon(dto.getIcon());
        menu.setIsVisible(dto.getIsVisible() != null ? dto.getIsVisible() : menu.getIsVisible());
        menuMapper.updateById(menu);
    }

    @Override
    public void delete(Long id) {
        menuMapper.deleteById(id);
    }
}
