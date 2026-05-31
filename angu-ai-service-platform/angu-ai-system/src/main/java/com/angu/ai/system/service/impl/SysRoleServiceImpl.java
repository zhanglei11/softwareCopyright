package com.angu.ai.system.service.impl;

import com.angu.ai.common.exception.ServiceException;
import com.angu.ai.system.domain.dto.RoleDTO;
import com.angu.ai.system.domain.entity.SysRole;
import com.angu.ai.system.domain.vo.RoleVO;
import com.angu.ai.system.mapper.SysRoleMapper;
import com.angu.ai.system.service.ISysRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl implements ISysRoleService {
    private final SysRoleMapper roleMapper;

    @Override public List<RoleVO> list() { return roleMapper.selectAll(); }

    @Override
    public RoleVO getById(Long id) {
        RoleVO vo = roleMapper.selectById(id);
        if (vo == null) throw new ServiceException(404, "角色不存在");
        return vo;
    }

    @Override
    @Transactional
    public void create(RoleDTO dto) {
        SysRole role = new SysRole();
        role.setRoleName(dto.getRoleName()); role.setRoleCode(dto.getRoleCode());
        role.setDescription(dto.getDescription());
        roleMapper.insert(role);
        if (dto.getMenuIds() != null) assignMenus(role.getId(), dto.getMenuIds());
        if (dto.getSceneIds() != null) assignScenes(role.getId(), dto.getSceneIds());
    }

    @Override
    public void update(Long id, RoleDTO dto) {
        SysRole e = roleMapper.selectEntityById(id);
        if (e == null) throw new ServiceException(404, "角色不存在");
        if (e.getBuiltin() == 1 && !e.getRoleCode().equals(dto.getRoleCode()))
            throw new ServiceException(400, "内置角色标识不可修改");
        e.setRoleName(dto.getRoleName()); e.setDescription(dto.getDescription());
        roleMapper.updateById(e);
    }

    @Override
    public void deleteById(Long id) {
        SysRole e = roleMapper.selectEntityById(id);
        if (e == null) throw new ServiceException(404, "角色不存在");
        if (e.getBuiltin() == 1) throw new ServiceException(400, "内置角色不可删除");
        roleMapper.deleteById(id);
    }

    @Override @Transactional
    public void assignMenus(Long roleId, List<Long> menuIds) {
        roleMapper.deleteRoleMenus(roleId);
        menuIds.forEach(mid -> roleMapper.insertRoleMenu(roleId, mid));
    }

    @Override @Transactional
    public void assignScenes(Long roleId, List<Long> sceneIds) {
        roleMapper.deleteRoleScenes(roleId);
        sceneIds.forEach(sid -> roleMapper.insertRoleScene(roleId, sid));
    }
}
