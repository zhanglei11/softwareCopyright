package com.imaging.scheduler.system.service.system;

import com.imaging.scheduler.common.core.TableDataInfo;
import com.imaging.scheduler.system.domain.system.SysUser;
import com.imaging.scheduler.system.dto.req.*;
import com.imaging.scheduler.system.dto.resp.LoginResp;

public interface SysUserService {
    LoginResp login(LoginReq req);
    LoginResp refreshToken(String refreshToken);
    TableDataInfo<SysUser> getUserList(UserQueryReq req);
    SysUser getUserById(Long id);
    void addUser(UserAddReq req);
    void editUser(Long id, UserEditReq req);
    void updateStatus(Long id, Integer status);
    void resetPassword(Long id);
    void deleteUser(Long id);
}
