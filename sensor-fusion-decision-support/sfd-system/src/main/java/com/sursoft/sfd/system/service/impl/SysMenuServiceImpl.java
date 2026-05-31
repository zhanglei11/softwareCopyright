package com.sursoft.sfd.system.service.impl;

import com.sursoft.sfd.common.exception.ServiceException;
import com.sursoft.sfd.common.utils.SnowflakeUtils;
import com.sursoft.sfd.system.domain.SysMenu;
import com.sursoft.sfd.system.mapper.SysMenuMapper;
import com.sursoft.sfd.system.service.ISysMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SysMenuServiceImpl implements ISysMenuService {
    private final SysMenuMapper menuMapper;

    @Override public List<SysMenu> tree() { return buildTree(menuMapper.selectAll(), 0L); }
    @Override public SysMenu getById(Long id) {
        SysMenu m = menuMapper.selectById(id);
        if (m == null) throw new ServiceException(404, "菜单不存在");
        return m;
    }
    @Override public void add(SysMenu menu, Long operatorId) {
        menu.setId(SnowflakeUtils.nextId());
        menu.setCreatedBy(operatorId);
        menu.setStatus(menu.getStatus() != null ? menu.getStatus() : 1);
        menuMapper.insert(menu);
    }
    @Override public void edit(Long id, SysMenu menu, Long operatorId) {
        getById(id);
        menu.setId(id);
        menu.setUpdatedBy(operatorId);
        menuMapper.update(menu);
    }
    @Override public void delete(Long id) {
        if (menuMapper.countChildren(id) > 0) throw new ServiceException(422, "菜单下有子节点，无法删除");
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
