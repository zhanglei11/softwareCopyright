package com.sursoft.iidp.system.sys.service.impl;

import com.sursoft.iidp.common.constant.HttpStatus;
import com.sursoft.iidp.common.exception.BusinessException;
import com.sursoft.iidp.system.sys.domain.SysUser;
import com.sursoft.iidp.system.sys.mapper.SysUserMapper;
import com.sursoft.iidp.system.sys.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysUserServiceImpl implements SysUserService {

    private final SysUserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<SysUser> listUsers(SysUser query) {
        return userMapper.selectList(query);
    }

    @Override
    public SysUser getUserById(Long id) {
        return userMapper.selectById(id);
    }

    @Override
    public int addUser(SysUser user) {
        if (userMapper.checkUsernameUnique(user.getUsername(), null) > 0) {
            throw new BusinessException(HttpStatus.CONFLICT, "用户名已存在: " + user.getUsername());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userMapper.insert(user);
    }

    @Override
    public int editUser(SysUser user) {
        if (userMapper.checkUsernameUnique(user.getUsername(), user.getId()) > 0) {
            throw new BusinessException(HttpStatus.CONFLICT, "用户名已存在: " + user.getUsername());
        }
        return userMapper.update(user);
    }

    @Override
    public int removeUser(Long id) {
        return userMapper.deleteById(id);
    }

    @Override
    public int updateStatus(Long id, Integer status) {
        return userMapper.updateStatus(id, status);
    }

    @Override
    public int resetPassword(Long id, String newPassword) {
        SysUser user = new SysUser();
        user.setId(id);
        user.setPassword(passwordEncoder.encode(newPassword));
        return userMapper.update(user);
    }
}
