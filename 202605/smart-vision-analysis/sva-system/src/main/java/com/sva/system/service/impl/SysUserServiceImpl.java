package com.sva.system.service.impl;

import com.sva.common.exception.ServiceException;
import com.sva.system.domain.SysUser;
import com.sva.system.mapper.SysUserMapper;
import com.sva.system.query.UserQuery;
import com.sva.system.service.ISysUserService;
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
    public List<SysUser> list(UserQuery query) {
        return userMapper.selectList(query);
    }

    @Override
    public SysUser getById(Long id) {
        SysUser user = userMapper.selectById(id);
        if (user == null) throw new ServiceException(404, "用户不存在");
        return user;
    }

    @Override
    @Transactional
    public void add(SysUser user, Long operatorId) {
        UserQuery q = new UserQuery();
        q.setUsername(user.getUsername());
        if (!userMapper.selectList(q).isEmpty()) throw new ServiceException(409, "用户名已存在");
        if (user.getPassword() == null || user.getPassword().isBlank()) {
            user.setPassword("Admin@123");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedBy(operatorId);
        user.setStatus(user.getStatus() == null ? 1 : user.getStatus());
        userMapper.insert(user);
    }

    @Override
    @Transactional
    public void update(SysUser user, Long operatorId) {
        getById(user.getId());
        userMapper.update(user);
    }

    @Override
    public void deleteById(Long id) {
        getById(id);
        userMapper.deleteById(id);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        getById(id);
        userMapper.updateStatus(id, status);
    }

    @Override
    public void resetPassword(Long id) {
        getById(id);
        userMapper.updatePassword(id, passwordEncoder.encode("Admin@123"));
    }

    @Override
    @Transactional
    public void assignRoles(Long userId, List<Long> roleIds) {
        userMapper.deleteUserRoles(userId);
        if (roleIds != null && !roleIds.isEmpty()) {
            userMapper.insertUserRoles(userId, roleIds);
        }
    }

    @Override
    public List<Long> getRoleIdsByUserId(Long userId) {
        return userMapper.selectRoleIdsByUserId(userId);
    }
}
