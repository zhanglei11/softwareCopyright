package com.sursoft.sfd.system.service.impl;

import com.sursoft.sfd.common.exception.ServiceException;
import com.sursoft.sfd.common.utils.SnowflakeUtils;
import com.sursoft.sfd.system.domain.SysRole;
import com.sursoft.sfd.system.mapper.SysRoleMapper;
import com.sursoft.sfd.system.service.ISysRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl implements ISysRoleService {
    private final SysRoleMapper roleMapper;

    @Override public List<SysRole> list(String keyword, Integer status) { return roleMapper.selectList(keyword, status); }
    @Override public SysRole getById(Long id) {
        SysRole r = roleMapper.selectById(id);
        if (r == null) throw new ServiceException(404, "角色不存在");
        return r;
    }
    @Override public void add(SysRole role, Long operatorId) {
        role.setId(SnowflakeUtils.nextId());
        role.setCreatedBy(operatorId);
        role.setStatus(role.getStatus() != null ? role.getStatus() : 1);
        roleMapper.insert(role);
    }
    @Override public void edit(Long id, SysRole role, Long operatorId) {
        getById(id);
        role.setId(id);
        role.setUpdatedBy(operatorId);
        roleMapper.update(role);
    }
    @Override @Transactional public void delete(Long id) {
        if (roleMapper.countUsersByRoleId(id) > 0)
            throw new ServiceException(422, "该角色下有绑定用户，无法删除");
        roleMapper.deleteRoleMenus(id);
        roleMapper.deleteById(id);
    }
    @Override @Transactional public void assignMenus(Long id, List<Long> menuIds) {
        getById(id);
        roleMapper.deleteRoleMenus(id);
        if (menuIds != null && !menuIds.isEmpty()) roleMapper.insertRoleMenus(id, menuIds);
    }
    @Override public List<Long> getMenuIds(Long id) { return roleMapper.selectMenuIdsByRoleId(id); }
}
