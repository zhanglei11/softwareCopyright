package com.sursoft.iidp.system.sys.service;

import com.sursoft.iidp.system.sys.domain.SysUser;
import java.util.List;

public interface SysUserService {
    List<SysUser> listUsers(SysUser query);
    SysUser getUserById(Long id);
    int addUser(SysUser user);
    int editUser(SysUser user);
    int removeUser(Long id);
    int updateStatus(Long id, Integer status);
    int resetPassword(Long id, String newPassword);
}
