package com.sva.framework.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class LoginUser implements UserDetails {

    private Long userId;
    private String username;
    private String password;
    private String realName;
    private Integer status;
    private Set<String> permissions;
    private List<String> roles;

    public LoginUser() {}

    public LoginUser(Long userId, String username, String password, String realName,
                     Integer status, Set<String> permissions, List<String> roles) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.realName = realName;
        this.status = status;
        this.permissions = permissions;
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (permissions == null) return List.of();
        return permissions.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override @JsonIgnore
    public String getPassword() { return password; }
    @Override
    public String getUsername() { return username; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return status == null || status == 1; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return status == null || status == 1; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getRealName() { return realName; }
    public void setRealName(String realName) { this.realName = realName; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Set<String> getPermissions() { return permissions; }
    public void setPermissions(Set<String> permissions) { this.permissions = permissions; }
    public List<String> getRoles() { return roles; }
    public void setRoles(List<String> roles) { this.roles = roles; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
}
