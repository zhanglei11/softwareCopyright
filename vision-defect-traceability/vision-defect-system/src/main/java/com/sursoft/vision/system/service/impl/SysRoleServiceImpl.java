package com.sursoft.vision.system.service.impl;

import com.sursoft.vision.common.core.TableDataInfo;
import com.sursoft.vision.common.exception.ServiceException;
import com.sursoft.vision.system.domain.SysRole;
import com.sursoft.vision.system.dto.RoleDTO;
import com.sursoft.vision.system.mapper.SysRoleMapper;
import com.sursoft.vision.system.service.SysRoleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl implements SysRoleService {

    private final SysRoleMapper roleMapper;

    @Override
    public TableDataInfo<SysRole> list(String roleName, Integer status, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<SysRole> list = roleMapper.selectList(roleName, status);
        return TableDataInfo.of(new PageInfo<>(list));
    }

    @Override
    public SysRole getById(Long id) {
        return roleMapper.selectById(id);
    }

    @Override
    public void add(RoleDTO dto) {
        SysRole role = new SysRole();
        role.setRoleName(dto.getRoleName());
        role.setRoleKey(dto.getRoleKey());
        role.setRemark(dto.getRemark());
        role.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);
        role.setIsDeleted(0);
        roleMapper.insert(role);
    }

    @Override
    public void edit(Long id, RoleDTO dto) {
        SysRole role = getById(id);
        if (role == null) throw new ServiceException("角色不存在");
        role.setRoleName(dto.getRoleName());
        role.setRoleKey(dto.getRoleKey());
        role.setRemark(dto.getRemark());
        roleMapper.updateById(role);
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
    public void delete(Long id) {
        SysRole role = new SysRole();
        role.setId(id);
        role.setIsDeleted(1);
        roleMapper.updateById(role);
    }
}
