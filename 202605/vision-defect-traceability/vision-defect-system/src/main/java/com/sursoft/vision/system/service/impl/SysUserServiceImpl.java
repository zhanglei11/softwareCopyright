package com.sursoft.vision.system.service.impl;

import com.sursoft.vision.common.constant.Constants;
import com.sursoft.vision.common.core.TableDataInfo;
import com.sursoft.vision.common.exception.ServiceException;
import com.sursoft.vision.system.domain.SysUser;
import com.sursoft.vision.system.dto.UserAddDTO;
import com.sursoft.vision.system.dto.UserEditDTO;
import com.sursoft.vision.system.mapper.SysUserMapper;
import com.sursoft.vision.system.service.SysUserService;
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

    @Override
    public TableDataInfo<SysUser> list(String username, String realName, Integer status,
                                        int pageNum, int pageSize) {
        com.github.pagehelper.PageHelper.startPage(pageNum, pageSize);
        List<SysUser> list = userMapper.selectList(username, realName, status);
        com.github.pagehelper.PageInfo<SysUser> pageInfo = new com.github.pagehelper.PageInfo<>(list);
        return TableDataInfo.of(pageInfo);
    }

    @Override
    public SysUser getById(Long id) {
        return userMapper.selectById(id);
    }

    @Override
    public SysUser getByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    @Override
    @Transactional
    public void add(UserAddDTO dto) {
        if (userMapper.selectByUsername(dto.getUsername()) != null) {
            throw new ServiceException("该账号已存在，请更换");
        }
        SysUser user = new SysUser();
        user.setUsername(dto.getUsername());
        user.setRealName(dto.getRealName());
        user.setPhone(dto.getPhone());
        user.setDepartment(dto.getDepartment());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setStatus(dto.getStatus());
        user.setIsDeleted(0);
        userMapper.insert(user);
        userMapper.insertUserRoles(user.getId(), dto.getRoleIds());
    }

    @Override
    @Transactional
    public void edit(Long id, UserEditDTO dto) {
        SysUser user = getById(id);
        if (user == null) throw new ServiceException("用户不存在");
        user.setRealName(dto.getRealName());
        user.setPhone(dto.getPhone());
        user.setDepartment(dto.getDepartment());
        user.setStatus(dto.getStatus());
        userMapper.updateById(user);
        userMapper.deleteUserRoles(id);
        userMapper.insertUserRoles(id, dto.getRoleIds());
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        SysUser user = new SysUser();
        user.setId(id);
        user.setStatus(status);
        userMapper.updateById(user);
    }

    @Override
    public void resetPassword(Long id) {
        SysUser user = new SysUser();
        user.setId(id);
        user.setPassword(passwordEncoder.encode("Init@123"));
        userMapper.updateById(user);
    }

    @Override
    public void delete(Long id) {
        SysUser user = new SysUser();
        user.setId(id);
        user.setIsDeleted(1);
        userMapper.updateById(user);
    }
}
