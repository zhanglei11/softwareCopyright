package com.vqcc.framework.security;

import com.vqcc.system.domain.SysUser;
import com.vqcc.system.mapper.SysUserMapper;
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = userMapper.selectByUsername(username);
        if (user == null) throw new UsernameNotFoundException("用户不存在：" + username);
        List<String> permissions = userMapper.selectPermissionsByUserId(user.getId());
        return new LoginUser(user, permissions);
    }
}
