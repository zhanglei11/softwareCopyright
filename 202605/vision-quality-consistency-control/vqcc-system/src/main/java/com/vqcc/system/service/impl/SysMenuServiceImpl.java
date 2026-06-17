package com.vqcc.system.service.impl;

import com.vqcc.common.exception.BusinessException;
import com.vqcc.system.domain.SysMenu;
import com.vqcc.system.mapper.SysMenuMapper;
import com.vqcc.system.service.ISysMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SysMenuServiceImpl implements ISysMenuService {

    private final SysMenuMapper menuMapper;

    @Override
    public List<SysMenu> tree() {
        return buildTree(menuMapper.selectAll(), 0L);
    }

    @Override
    public List<SysMenu> treeByUserId(Long userId) {
        return buildTree(menuMapper.selectByUserId(userId), 0L);
    }

    @Override
    public void create(SysMenu menu) {
        if (menu.getParentId() == null) menu.setParentId(0L);
        menu.setStatus(1);
        menuMapper.insert(menu);
    }

    @Override
    public void update(SysMenu menu) {
        if (menuMapper.selectById(menu.getId()) == null) throw new BusinessException(404, "菜单不存在");
        menuMapper.update(menu);
    }

    @Override
    public void delete(Long id) {
        if (menuMapper.selectById(id) == null) throw new BusinessException(404, "菜单不存在");
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
