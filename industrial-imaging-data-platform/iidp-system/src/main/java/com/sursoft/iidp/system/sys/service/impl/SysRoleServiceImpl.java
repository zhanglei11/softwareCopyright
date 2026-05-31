package com.sursoft.iidp.system.sys.service.impl;

import com.sursoft.iidp.system.sys.domain.SysRole;
import com.sursoft.iidp.system.sys.mapper.SysRoleMapper;
import com.sursoft.iidp.system.sys.service.SysRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl implements SysRoleService {
    private final SysRoleMapper roleMapper;

    @Override public List<SysRole> listRoles(SysRole query) { return roleMapper.selectList(query); }
    @Override public SysRole getRoleById(Long id) { return roleMapper.selectById(id); }

    @Override
    @Transactional
    public int addRole(SysRole role) {
        int rows = roleMapper.insert(role);
        if (role.getMenuIds() != null && !role.getMenuIds().isEmpty()) {
            roleMapper.insertRoleMenuBatch(role.getId(), role.getMenuIds());
        }
        return rows;
    }

    @Override
    @Transactional
    public int editRole(SysRole role) {
        int rows = roleMapper.update(role);
        roleMapper.deleteRoleMenuByRoleId(role.getId());
        if (role.getMenuIds() != null && !role.getMenuIds().isEmpty()) {
            roleMapper.insertRoleMenuBatch(role.getId(), role.getMenuIds());
        }
        return rows;
    }

    @Override public int removeRole(Long id) { return roleMapper.deleteById(id); }
}
