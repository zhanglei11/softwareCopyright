package com.angu.matcher.framework.security;

import com.angu.matcher.system.domain.SysUser;
import com.angu.matcher.system.mapper.SysRoleMapper;
import com.angu.matcher.system.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final SysUserMapper userMapper;
    private final SysRoleMapper roleMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = userMapper.selectByUsername(username);
        if (user == null) throw new UsernameNotFoundException("用户不存在: " + username);
        List<String> perms = roleMapper.selectPermCodesByUserId(user.getId());
        return new LoginUser(user, perms);
    }
}
