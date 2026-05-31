package com.vqcc.system.service.impl;

import com.vqcc.common.exception.BusinessException;
import com.vqcc.system.domain.SysRole;
import com.vqcc.system.mapper.SysRoleMapper;
import com.vqcc.system.service.ISysRoleService;
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
        if (role == null) throw new BusinessException(404, "角色不存在");
        return role;
    }

    @Override
    public void create(SysRole role, Long operatorId) {
        role.setCreatedBy(operatorId);
        role.setUpdatedBy(operatorId);
        role.setStatus(1);
        roleMapper.insert(role);
    }

    @Override
    public void update(SysRole role, Long operatorId) {
        getById(role.getId());
        role.setUpdatedBy(operatorId);
        roleMapper.update(role);
    }

    @Override
    public void delete(Long id) {
        getById(id);
        roleMapper.deleteRoleMenus(id);
        roleMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void assignMenus(Long roleId, List<Long> menuIds) {
        roleMapper.deleteRoleMenus(roleId);
        if (menuIds != null && !menuIds.isEmpty()) {
            roleMapper.assignMenus(roleId, menuIds);
        }
    }

    @Override
    public List<Long> getMenuIds(Long roleId) {
        return roleMapper.selectMenuIdsByRoleId(roleId);
    }
}
