package com.sursoft.vision.system.service;

import com.sursoft.vision.common.core.AjaxResult;
import com.sursoft.vision.common.core.TableDataInfo;
import com.sursoft.vision.system.domain.SysRole;
import com.sursoft.vision.system.dto.RoleDTO;
import java.util.List;

public interface SysRoleService {
    TableDataInfo<SysRole> list(String roleName, Integer status, int pageNum, int pageSize);
    SysRole getById(Long id);
    void add(RoleDTO dto);
    void edit(Long id, RoleDTO dto);
    void assignMenus(Long roleId, List<Long> menuIds);
    void delete(Long id);
}
