package com.angu.ai.framework.security.service;

import com.angu.ai.common.core.domain.LoginUser;
import com.angu.ai.common.exception.ServiceException;
import com.angu.ai.system.domain.entity.SysUser;
import com.angu.ai.system.mapper.SysRoleMapper;
import com.angu.ai.system.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final SysUserMapper userMapper;
    private final SysRoleMapper roleMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = userMapper.selectEntityByUsername(username);
        if (user == null) throw new UsernameNotFoundException("用户不存在: " + username);
        return buildLoginUser(user);
    }

    public UserDetails loadUserByUserId(Long userId) {
        SysUser user = userMapper.selectEntityById(userId);
        if (user == null) throw new ServiceException(401, "用户不存在");
        return buildLoginUser(user);
    }

    private LoginUser buildLoginUser(SysUser user) {
        List<String> roles = new java.util.ArrayList<>(roleMapper.selectRoleCodesByUserId(user.getId()));
        Set<String> perms = roleMapper.selectPermsByUserId(user.getId());
        List<GrantedAuthority> authorities = perms.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(user.getId());
        loginUser.setUsername(user.getUsername());
        loginUser.setRealName(user.getRealName());
        loginUser.setPassword(user.getPassword());
        loginUser.setRoles(roles);
        loginUser.setPermissions(perms);
        loginUser.setAuthorities(authorities);
        return loginUser;
    }
}
