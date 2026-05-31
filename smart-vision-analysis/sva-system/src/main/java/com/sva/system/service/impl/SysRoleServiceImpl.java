package com.sva.system.service.impl;

import com.sva.common.exception.ServiceException;
import com.sva.system.domain.SysRole;
import com.sva.system.mapper.SysRoleMapper;
import com.sva.system.service.ISysRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl implements ISysRoleService {

    private final SysRoleMapper roleMapper;

    @Override
    public List<SysRole> list(String roleName, Integer status) {
        return roleMapper.selectList(roleName, status);
    }

    @Override
    public SysRole getById(Long id) {
        SysRole role = roleMapper.selectById(id);
        if (role == null) throw new ServiceException(404, "角色不存在");
        return role;
    }

    @Override
    public void add(SysRole role, Long operatorId) {
        role.setStatus(role.getStatus() == null ? 1 : role.getStatus());
        roleMapper.insert(role);
    }

    @Override
    public void update(SysRole role, Long operatorId) {
        getById(role.getId());
        roleMapper.update(role);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (roleMapper.countUsersByRoleId(id) > 0) throw new ServiceException(400, "角色已被用户使用，无法删除");
        roleMapper.deleteRoleMenus(id);
        roleMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void assignMenus(Long roleId, List<Long> menuIds) {
        roleMapper.deleteRoleMenus(roleId);
        if (menuIds != null && !menuIds.isEmpty()) {
            roleMapper.insertRoleMenus(roleId, menuIds);
        }
    }

    @Override
    public List<Long> getMenuIds(Long roleId) {
        return roleMapper.selectMenuIdsByRoleId(roleId);
    }
}
