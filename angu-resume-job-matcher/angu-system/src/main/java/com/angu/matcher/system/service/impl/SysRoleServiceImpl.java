package com.angu.matcher.system.service.impl;

import com.angu.matcher.common.exception.ServiceException;
import com.angu.matcher.system.domain.SysRole;
import com.angu.matcher.system.mapper.SysRoleMapper;
import com.angu.matcher.system.service.ISysRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl implements ISysRoleService {

    private final SysRoleMapper roleMapper;

    @Override
    public List<SysRole> listRoles() {
        return roleMapper.selectAll();
    }

    @Override
    public SysRole getById(Long id) {
        SysRole role = roleMapper.selectById(id);
        if (role == null) throw new ServiceException(404, "角色不存在");
        return role;
    }

    @Override
    public void createRole(SysRole role) {
        if (roleMapper.selectByRoleCode(role.getRoleCode()) != null) {
            throw new ServiceException(409, "角色标识已存在");
        }
        role.setStatus(1);
        roleMapper.insert(role);
    }

    @Override
    public void updateRole(SysRole role) {
        SysRole existing = getById(role.getId());
        if (existing.getBuiltin() == 1) throw new ServiceException(403, "内置角色不可编辑");
        roleMapper.update(role);
    }

    @Override
    @Transactional
    public void deleteRole(Long id) {
        SysRole role = getById(id);
        if (role.getBuiltin() == 1) throw new ServiceException(403, "内置角色不可删除");
        int count = roleMapper.countUserByRoleId(id);
        if (count > 0) throw new ServiceException(409, "该角色下有 " + count + " 个用户，请先解绑用户");
        roleMapper.deleteById(id);
        roleMapper.deleteRoleMenus(id);
    }

    @Override
    @Transactional
    public void assignMenus(Long roleId, List<Long> menuIds) {
        getById(roleId);
        roleMapper.deleteRoleMenus(roleId);
        if (menuIds != null && !menuIds.isEmpty()) {
            roleMapper.insertRoleMenus(roleId, menuIds);
        }
    }

    @Override
    public List<Long> getMenuIdsByRoleId(Long roleId) {
        return roleMapper.selectMenuIdsByRoleId(roleId);
    }

    @Override
    public List<String> getPermCodesByUserId(Long userId) {
        return roleMapper.selectPermCodesByUserId(userId);
    }
}
