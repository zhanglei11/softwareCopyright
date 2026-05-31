package com.angu.ai.system.service;

import com.angu.ai.system.domain.dto.RoleDTO;
import com.angu.ai.system.domain.vo.RoleVO;

import java.util.List;

public interface ISysRoleService {
    List<RoleVO> list();
    RoleVO getById(Long id);
    void create(RoleDTO dto);
    void update(Long id, RoleDTO dto);
    void deleteById(Long id);
    void assignMenus(Long roleId, List<Long> menuIds);
    void assignScenes(Long roleId, List<Long> sceneIds);
}
