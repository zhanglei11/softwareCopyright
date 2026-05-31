package com.angu.matcher.system.service.impl;

import com.angu.matcher.common.exception.ServiceException;
import com.angu.matcher.system.domain.SysUser;
import com.angu.matcher.system.dto.*;
import com.angu.matcher.system.mapper.SysUserMapper;
import com.angu.matcher.system.service.ISysUserService;
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
    public List<SysUser> listUsers(String username, String phone, Integer status) {
        return userMapper.selectList(username, phone, status);
    }

    @Override
    public SysUser getById(Long id) {
        SysUser user = userMapper.selectById(id);
        if (user == null) throw new ServiceException(404, "用户不存在");
        return user;
    }

    @Override
    public SysUser getByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    @Override
    @Transactional
    public void createUser(UserCreateRequest req) {
        if (userMapper.selectByUsername(req.getUsername()) != null) {
            throw new ServiceException(409, "用户名已存在");
        }
        SysUser user = new SysUser();
        user.setUsername(req.getUsername());
        user.setRealName(req.getRealName());
        user.setPhone(req.getPhone());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setStatus(1);
        userMapper.insert(user);
        if (req.getRoleIds() != null && !req.getRoleIds().isEmpty()) {
            userMapper.insertUserRoles(user.getId(), req.getRoleIds());
        }
    }

    @Override
    @Transactional
    public void updateUser(Long id, UserUpdateRequest req) {
        SysUser user = getById(id);
        user.setRealName(req.getRealName());
        user.setPhone(req.getPhone());
        if (req.getStatus() != null) user.setStatus(req.getStatus());
        userMapper.update(user);
        if (req.getRoleIds() != null) {
            userMapper.deleteUserRoles(id);
            if (!req.getRoleIds().isEmpty()) {
                userMapper.insertUserRoles(id, req.getRoleIds());
            }
        }
    }

    @Override
    @Transactional
    public void deleteUser(Long id, Long currentUserId) {
        if (id.equals(currentUserId)) throw new ServiceException(400, "不能删除当前登录账号");
        getById(id);
        userMapper.deleteById(id);
        userMapper.deleteUserRoles(id);
    }

    @Override
    public void resetPassword(Long id, String newPassword) {
        SysUser user = getById(id);
        user.setPassword(passwordEncoder.encode(newPassword));
        userMapper.update(user);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        SysUser user = getById(id);
        user.setStatus(status);
        userMapper.update(user);
    }

    @Override
    public List<Long> getRoleIdsByUserId(Long userId) {
        return userMapper.selectRoleIdsByUserId(userId);
    }
}
