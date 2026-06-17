package com.sursoft.vision.framework.security;

import com.sursoft.vision.system.domain.SysUser;
import com.sursoft.vision.system.mapper.SysUserMapper;
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
        if (user == null || user.getIsDeleted() == 1) {
            throw new UsernameNotFoundException("用户不存在：" + username);
        }
        List<String> roles = userMapper.selectRoleKeysByUserId(user.getId());
        List<String> perms = userMapper.selectPermsByUserId(user.getId());
        return new LoginUser(user, roles, perms);
    }
}
