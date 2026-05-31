package com.sva.framework.security;

import com.sva.framework.security.mapper.SysUserSecurityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private SysUserSecurityMapper sysUserSecurityMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUserSecurityMapper.UserSecurityInfo userInfo = sysUserSecurityMapper.findByUsername(username);
        if (userInfo == null) {
            throw new UsernameNotFoundException("用户不存在：" + username);
        }
        if (userInfo.getStatus() == 0) {
            throw new UsernameNotFoundException("账号已被禁用");
        }
        List<String> roles = sysUserSecurityMapper.findRolesByUserId(userInfo.getId());
        Set<String> permissions = new HashSet<>(sysUserSecurityMapper.findPermsByUserId(userInfo.getId()));
        return new LoginUser(userInfo.getId(), userInfo.getUsername(), userInfo.getPassword(),
                userInfo.getRealName(), userInfo.getStatus(), permissions, roles);
    }
}
