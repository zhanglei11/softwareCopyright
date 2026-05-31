package com.angu.ai.system.service.impl;

import com.angu.ai.common.constant.UserConstants;
import com.angu.ai.common.core.page.TableDataInfo;
import com.angu.ai.common.exception.ServiceException;
import com.angu.ai.system.domain.dto.UserCreateDTO;
import com.angu.ai.system.domain.entity.SysUser;
import com.angu.ai.system.domain.query.UserQuery;
import com.angu.ai.system.domain.vo.UserVO;
import com.angu.ai.system.mapper.AiCallLogMapper;
import com.angu.ai.system.mapper.SysUserMapper;
import com.angu.ai.system.service.ISysUserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SysUserServiceImpl implements ISysUserService {

    private final SysUserMapper userMapper;
    private final AiCallLogMapper callLogMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public TableDataInfo<UserVO> pageList(UserQuery query) {
        PageHelper.startPage(query.getPage(), query.getSize());
        List<UserVO> list = userMapper.selectPage(query);
        PageInfo<UserVO> info = new PageInfo<>(list);
        return TableDataInfo.of(info.getTotal(), info.getPages(), list);
    }

    @Override
    public UserVO getById(Long id) {
        UserVO vo = userMapper.selectById(id);
        if (vo == null) throw new ServiceException(404, "用户不存在");
        return vo;
    }

    @Override
    @Transactional
    public void create(UserCreateDTO dto) {
        if (userMapper.selectEntityByUsername(dto.getUsername()) != null)
            throw new ServiceException(400, "账号已存在");
        SysUser user = new SysUser();
        user.setUsername(dto.getUsername()); user.setRealName(dto.getRealName());
        user.setPhone(dto.getPhone()); user.setEmail(dto.getEmail());
        user.setDepartment(dto.getDepartment()); user.setDailyLimit(dto.getDailyLimit());
        user.setPassword(passwordEncoder.encode(dto.getPassword() != null ? dto.getPassword() : UserConstants.DEFAULT_PASSWORD));
        user.setStatus(1);
        userMapper.insert(user);
        for (Long roleId : dto.getRoleIds()) userMapper.insertUserRole(user.getId(), roleId);
    }

    @Override
    @Transactional
    public void update(Long id, UserCreateDTO dto) {
        getById(id);
        SysUser user = new SysUser();
        user.setId(id); user.setRealName(dto.getRealName());
        user.setPhone(dto.getPhone()); user.setEmail(dto.getEmail());
        user.setDepartment(dto.getDepartment()); user.setDailyLimit(dto.getDailyLimit());
        userMapper.updateById(user);
        userMapper.deleteUserRoles(id);
        for (Long roleId : dto.getRoleIds()) userMapper.insertUserRole(id, roleId);
    }

    @Override public void deleteById(Long id) { getById(id); userMapper.deleteById(id); }

    @Override
    public void resetPassword(Long id) {
        getById(id);
        SysUser u = new SysUser(); u.setId(id);
        u.setPassword(passwordEncoder.encode(UserConstants.DEFAULT_PASSWORD));
        userMapper.updateById(u);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        getById(id);
        SysUser u = new SysUser(); u.setId(id); u.setStatus(status);
        userMapper.updateById(u);
    }

    @Override
    public Map<String, Object> getUserUsage(Long id) {
        getById(id);
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("todayCallCount", callLogMapper.countTodayByUser(id));
        map.put("monthCallCount", callLogMapper.countThisMonthByUser(id));
        map.put("monthTokenCount", callLogMapper.countTokenThisMonthByUser(id) != null ? callLogMapper.countTokenThisMonthByUser(id) : 0L);
        return map;
    }
}
