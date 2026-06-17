package com.angu.matcher.system.service;

import com.angu.matcher.system.domain.SysUser;
import com.angu.matcher.system.dto.*;

import java.util.List;

public interface ISysUserService {
    List<SysUser> listUsers(String username, String phone, Integer status);
    SysUser getById(Long id);
    SysUser getByUsername(String username);
    void createUser(UserCreateRequest req);
    void updateUser(Long id, UserUpdateRequest req);
    void deleteUser(Long id, Long currentUserId);
    void resetPassword(Long id, String newPassword);
    void updateStatus(Long id, Integer status);
    List<Long> getRoleIdsByUserId(Long userId);
}
