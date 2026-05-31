package com.sursoft.vision.system.service;

import com.sursoft.vision.common.core.TableDataInfo;
import com.sursoft.vision.system.domain.SysUser;
import com.sursoft.vision.system.dto.UserAddDTO;
import com.sursoft.vision.system.dto.UserEditDTO;

public interface SysUserService {
    TableDataInfo<SysUser> list(String username, String realName, Integer status, int pageNum, int pageSize);
    SysUser getById(Long id);
    SysUser getByUsername(String username);
    void add(UserAddDTO dto);
    void edit(Long id, UserEditDTO dto);
    void updateStatus(Long id, Integer status);
    void resetPassword(Long id);
    void delete(Long id);
}
