package com.vqcc.system.service.impl;

import com.vqcc.common.exception.BusinessException;
import com.vqcc.system.domain.SysUser;
import com.vqcc.system.dto.request.UserCreateReq;
import com.vqcc.system.dto.request.UserUpdateReq;
import com.vqcc.system.mapper.SysUserMapper;
import com.vqcc.system.service.ISysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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
        if (user == null) throw new BusinessException(404, "用户不存在");
        return user;
    }

    @Override
    public SysUser getByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    @Override
    public void create(UserCreateReq req, Long operatorId) {
        if (userMapper.selectByUsername(req.getUsername()) != null) {
            throw new BusinessException(409, "用户名已存在：" + req.getUsername());
        }
        SysUser user = new SysUser();
        user.setUsername(req.getUsername());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setRealName(req.getRealName());
        user.setPhone(req.getPhone());
        user.setDept(req.getDept());
        user.setStatus(1);
        user.setCreatedBy(operatorId);
        user.setUpdatedBy(operatorId);
        userMapper.insert(user);
    }

    @Override
    public void update(UserUpdateReq req, Long operatorId) {
        SysUser user = getById(req.getId());
        user.setRealName(req.getRealName());
        user.setPhone(req.getPhone());
        user.setDept(req.getDept());
        user.setStatus(req.getStatus());
        user.setUpdatedBy(operatorId);
        userMapper.update(user);
    }

    @Override
    public void delete(Long id) {
        getById(id);
        userMapper.deleteById(id);
    }

    @Override
    public void resetPassword(Long id) {
        getById(id);
        SysUser user = new SysUser();
        user.setId(id);
        user.setPassword(passwordEncoder.encode("Admin@123"));
        user.setUpdatedBy(id);
        userMapper.update(user);
    }

    @Override
    public List<String> getPermissions(Long userId) {
        return userMapper.selectPermissionsByUserId(userId);
    }

    @Override
    public List<String> getRoleCodes(Long userId) {
        return userMapper.selectRoleCodesByUserId(userId);
    }
}
