package com.sursoft.sfd.system.service;
import com.sursoft.sfd.system.domain.SysUser;
import java.util.List;
public interface ISysUserService {
    List<SysUser> list(String username, String realName, Integer status);
    SysUser getById(Long id);
    void add(SysUser user, List<Long> roleIds, Long operatorId);
    void edit(Long id, SysUser user, Long operatorId);
    void updateStatus(Long id, Integer status, Long operatorId);
    void resetPassword(Long id, Long operatorId);
    void delete(Long id);
}
