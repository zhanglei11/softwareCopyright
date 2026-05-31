package com.sursoft.sfd.system.service.impl;

import com.sursoft.sfd.common.exception.ServiceException;
import com.sursoft.sfd.common.utils.SnowflakeUtils;
import com.sursoft.sfd.system.domain.SysUser;
import com.sursoft.sfd.system.mapper.SysUserMapper;
import com.sursoft.sfd.system.service.ISysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SysUserServiceImpl implements ISysUserService {
    private final SysUserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<SysUser> list(String username, String realName, Integer status) {
        return userMapper.selectList(username, realName, status);
    }

    @Override
    public SysUser getById(Long id) {
        SysUser user = userMapper.selectById(id);
        if (user == null) throw new ServiceException(404, "用户不存在");
        List<String> roles = userMapper.selectRoleCodesByUserId(id);
        return user;
    }

    @Override
    @Transactional
    public void add(SysUser user, List<Long> roleIds, Long operatorId) {
        if (userMapper.selectByUsername(user.getUsername()) != null)
            throw new ServiceException(409, "登录账号已存在");
        user.setId(SnowflakeUtils.nextId());
        user.setPassword(passwordEncoder.encode(
                user.getPassword() != null ? user.getPassword() : "Admin@123"));
        user.setStatus(user.getStatus() != null ? user.getStatus() : 1);
        user.setCreatedBy(operatorId);
        userMapper.insert(user);
        if (roleIds != null && !roleIds.isEmpty()) {
            userMapper.insertUserRoles(user.getId(), roleIds);
        }
    }

    @Override
    @Transactional
    public void edit(Long id, SysUser user, Long operatorId) {
        SysUser exist = userMapper.selectById(id);
        if (exist == null) throw new ServiceException(404, "用户不存在");
        user.setId(id);
        user.setUpdatedBy(operatorId);
        userMapper.update(user);
        if (user.getRoles() != null) {
            userMapper.deleteUserRoles(id);
            List<Long> roleIds = user.getRoles().stream()
                    .map(r -> r.getId()).toList();
            if (!roleIds.isEmpty()) userMapper.insertUserRoles(id, roleIds);
        }
    }

    @Override
    public void updateStatus(Long id, Integer status, Long operatorId) {
        if (userMapper.selectById(id) == null) throw new ServiceException(404, "用户不存在");
        userMapper.updateStatus(id, status, operatorId);
    }

    @Override
    public void resetPassword(Long id, Long operatorId) {
        if (userMapper.selectById(id) == null) throw new ServiceException(404, "用户不存在");
        userMapper.resetPassword(id, passwordEncoder.encode("Admin@123"), operatorId);
    }

    @Override
    public void delete(Long id) {
        if (userMapper.selectById(id) == null) throw new ServiceException(404, "用户不存在");
        userMapper.deleteUserRoles(id);
        userMapper.deleteById(id);
    }
}
