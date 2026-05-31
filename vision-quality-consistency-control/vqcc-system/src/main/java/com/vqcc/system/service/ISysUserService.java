package com.vqcc.system.service;

import com.vqcc.system.domain.SysUser;
import com.vqcc.system.dto.request.UserCreateReq;
import com.vqcc.system.dto.request.UserUpdateReq;
import java.util.List;

public interface ISysUserService {
    List<SysUser> list(String username, String realName, Integer status);
    SysUser getById(Long id);
    SysUser getByUsername(String username);
    void create(UserCreateReq req, Long operatorId);
    void update(UserUpdateReq req, Long operatorId);
    void delete(Long id);
    void resetPassword(Long id);
    List<String> getPermissions(Long userId);
    List<String> getRoleCodes(Long userId);
}
