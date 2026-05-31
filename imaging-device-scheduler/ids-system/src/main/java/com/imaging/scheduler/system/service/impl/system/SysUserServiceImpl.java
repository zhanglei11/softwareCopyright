package com.imaging.scheduler.system.service.impl.system;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imaging.scheduler.common.constant.SecurityConstants;
import com.imaging.scheduler.common.core.TableDataInfo;
import com.imaging.scheduler.common.exception.BusinessException;
import com.imaging.scheduler.system.domain.system.SysUser;
import com.imaging.scheduler.system.dto.req.*;
import com.imaging.scheduler.system.dto.resp.LoginResp;
import com.imaging.scheduler.system.mapper.system.SysUserMapper;
import com.imaging.scheduler.system.service.system.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysUserServiceImpl implements SysUserService {

    private final SysUserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    // JwtUtil 通过 framework 注入，这里声明接口避免循环依赖
    // 实际 login 逻辑由 AuthService (framework 层) 处理
    // 此处提供用户查询和管理功能

    @Override
    public LoginResp login(LoginReq req) {
        // 由 AuthService 调用，此处仅做基础校验
        throw new UnsupportedOperationException("login should be handled by AuthService");
    }

    @Override
    public LoginResp refreshToken(String refreshToken) {
        throw new UnsupportedOperationException("refreshToken should be handled by AuthService");
    }

    @Override
    public TableDataInfo<SysUser> getUserList(UserQueryReq req) {
        PageHelper.startPage(req.getPage(), req.getPageSize());
        List<SysUser> list = userMapper.selectList(req);
        PageInfo<SysUser> pageInfo = new PageInfo<>(list);
        return TableDataInfo.success(pageInfo.getTotal(), req.getPage(), req.getPageSize(), list);
    }

    @Override
    public SysUser getUserById(Long id) {
        SysUser user = userMapper.selectById(id);
        if (user == null) throw new BusinessException(404, "用户不存在");
        return user;
    }

    @Override
    @Transactional
    public void addUser(UserAddReq req) {
        int count = userMapper.countByUsername(req.getUsername(), null);
        if (count > 0) throw new BusinessException(409, "账号已存在");
        SysUser user = new SysUser();
        user.setUsername(req.getUsername());
        user.setRealName(req.getRealName());
        user.setPhone(req.getPhone());
        user.setDepartment(req.getDepartment());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setStatus(req.getStatus());
        user.setIsDeleted(0);
        userMapper.insert(user);
        if (req.getRoleIds() != null && !req.getRoleIds().isEmpty()) {
            userMapper.selectById(user.getId()); // get id after insert
        }
    }

    @Override
    @Transactional
    public void editUser(Long id, UserEditReq req) {
        SysUser user = getUserById(id);
        user.setRealName(req.getRealName());
        user.setPhone(req.getPhone());
        user.setDepartment(req.getDepartment());
        user.setStatus(req.getStatus());
        userMapper.update(user);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        SysUser user = getUserById(id);
        user.setStatus(status);
        userMapper.update(user);
    }

    @Override
    public void resetPassword(Long id) {
        SysUser user = getUserById(id);
        user.setPassword(passwordEncoder.encode(SecurityConstants.DEFAULT_PASSWORD));
        userMapper.update(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        getUserById(id);
        userMapper.deleteById(id);
    }

    public SysUser loadUserByUsername(String username) {
        return userMapper.selectByUsername(username);
    }
}
