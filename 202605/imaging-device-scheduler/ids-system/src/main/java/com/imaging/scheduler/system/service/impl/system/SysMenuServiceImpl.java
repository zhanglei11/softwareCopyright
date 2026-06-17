package com.imaging.scheduler.system.service.impl.system;

import com.imaging.scheduler.common.exception.BusinessException;
import com.imaging.scheduler.system.domain.system.SysMenu;
import com.imaging.scheduler.system.dto.req.MenuAddReq;
import com.imaging.scheduler.system.mapper.system.SysMenuMapper;
import com.imaging.scheduler.system.service.system.SysMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysMenuServiceImpl implements SysMenuService {
    private final SysMenuMapper menuMapper;

    @Override
    public List<SysMenu> getMenuTree() {
        return menuMapper.selectAll();
    }

    @Override
    public void addMenu(MenuAddReq req) {
        SysMenu menu = new SysMenu();
        menu.setParentId(req.getParentId());
        menu.setMenuName(req.getMenuName());
        menu.setMenuType(req.getMenuType());
        menu.setPath(req.getPath());
        menu.setPermission(req.getPermission());
        menu.setIcon(req.getIcon());
        menu.setSort(req.getSort());
        menu.setStatus(req.getStatus());
        menu.setIsDeleted(0);
        menuMapper.insert(menu);
    }

    @Override
    public void editMenu(Long id, MenuAddReq req) {
        SysMenu menu = menuMapper.selectById(id);
        if (menu == null) throw new BusinessException(404, "菜单不存在");
        menu.setMenuName(req.getMenuName());
        menu.setPath(req.getPath());
        menu.setPermission(req.getPermission());
        menu.setIcon(req.getIcon());
        menu.setSort(req.getSort());
        menu.setStatus(req.getStatus());
        menuMapper.update(menu);
    }

    @Override
    @Transactional
    public void deleteMenu(Long id) {
        int count = menuMapper.countChildByParentId(id);
        if (count > 0) throw new BusinessException(422, "该菜单下有子节点，无法删除");
        menuMapper.deleteById(id);
    }
}
