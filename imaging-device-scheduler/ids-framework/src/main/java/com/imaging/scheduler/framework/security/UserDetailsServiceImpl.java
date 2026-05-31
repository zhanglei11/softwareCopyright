package com.imaging.scheduler.framework.security;

import com.imaging.scheduler.framework.security.model.LoginUser;
import com.imaging.scheduler.system.domain.system.SysUser;
import com.imaging.scheduler.system.mapper.system.SysMenuMapper;
import com.imaging.scheduler.system.mapper.system.SysUserMapper;
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
    private final SysMenuMapper menuMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = userMapper.selectByUsername(username);
        if (user == null) throw new UsernameNotFoundException("用户不存在: " + username);
        List<String> perms = menuMapper.selectPermsByUserId(user.getId());
        return new LoginUser(user, perms);
    }
}
