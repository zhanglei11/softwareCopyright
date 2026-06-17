package com.imaging.scheduler.system.service.impl.system;

import com.imaging.scheduler.common.exception.BusinessException;
import com.imaging.scheduler.system.domain.system.SysRole;
import com.imaging.scheduler.system.dto.req.RoleAddReq;
import com.imaging.scheduler.system.dto.req.RoleMenuReq;
import com.imaging.scheduler.system.mapper.system.SysRoleMapper;
import com.imaging.scheduler.system.service.system.SysRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl implements SysRoleService {
    private final SysRoleMapper roleMapper;

    @Override
    public List<SysRole> getRoleList() {
        return roleMapper.selectList();
    }

    @Override
    @Transactional
    public void addRole(RoleAddReq req) {
        SysRole role = new SysRole();
        role.setRoleName(req.getRoleName());
        role.setRoleCode(req.getRoleCode());
        role.setDescription(req.getDescription());
        role.setStatus(req.getStatus());
        role.setIsDeleted(0);
        roleMapper.insert(role);
    }

    @Override
    @Transactional
    public void editRole(Long id, RoleAddReq req) {
        SysRole role = roleMapper.selectById(id);
        if (role == null) throw new BusinessException(404, "角色不存在");
        role.setRoleName(req.getRoleName());
        role.setDescription(req.getDescription());
        role.setStatus(req.getStatus());
        roleMapper.update(role);
    }

    @Override
    @Transactional
    public void assignMenus(Long id, RoleMenuReq req) {
        roleMapper.deleteMenuRoleByRoleId(id);
        if (req.getMenuIds() != null && !req.getMenuIds().isEmpty()) {
            roleMapper.insertRoleMenus(id, req.getMenuIds());
        }
    }

    @Override
    @Transactional
    public void deleteRole(Long id) {
        int count = roleMapper.countUsersByRoleId(id);
        if (count > 0) throw new BusinessException(422, "该角色下有绑定用户，无法删除");
        roleMapper.deleteMenuRoleByRoleId(id);
        roleMapper.deleteById(id);
    }
}
