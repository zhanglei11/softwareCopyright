package com.sursoft.vision.framework.security;

import com.sursoft.vision.system.domain.SysUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class LoginUser implements UserDetails {

    private SysUser user;
    private List<String> roles;
    private List<String> perms;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return perms.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.getStatus() == 1;
    }

    @Override
    public boolean isEnabled() {
        return user.getStatus() == 1;
    }
}
