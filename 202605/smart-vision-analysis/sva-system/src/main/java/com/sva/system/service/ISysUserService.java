package com.sva.system.service;

import com.sva.system.domain.SysUser;
import com.sva.system.query.UserQuery;
import java.util.List;

public interface ISysUserService {
    List<SysUser> list(UserQuery query);
    SysUser getById(Long id);
    void add(SysUser user, Long operatorId);
    void update(SysUser user, Long operatorId);
    void deleteById(Long id);
    void updateStatus(Long id, Integer status);
    void resetPassword(Long id);
    void assignRoles(Long userId, List<Long> roleIds);
    List<Long> getRoleIdsByUserId(Long userId);
}
