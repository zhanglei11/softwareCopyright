package com.sursoft.iidp.system.sys.service.impl;

import com.sursoft.iidp.framework.security.LoginUser;
import com.sursoft.iidp.system.sys.domain.SysUser;
import com.sursoft.iidp.system.sys.mapper.SysMenuMapper;
import com.sursoft.iidp.system.sys.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final SysUserMapper sysUserMapper;
    private final SysMenuMapper sysMenuMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = sysUserMapper.selectByUsername(username);
        if (user == null) throw new UsernameNotFoundException("用户不存在: " + username);
        if (user.getStatus() == 0) throw new UsernameNotFoundException("账号已停用: " + username);
        Set<String> permissions = sysMenuMapper.selectPermsByUserId(user.getId());
        return new LoginUser(user.getId(), user.getUsername(), user.getPassword(), permissions);
    }
}
