package com.sva.framework.security.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysUserSecurityMapper {

    @Select("SELECT id, username, password, real_name AS realName, status FROM sys_user WHERE username = #{username} AND deleted = 0")
    UserSecurityInfo findByUsername(String username);

    @Select("SELECT r.role_code FROM sys_role r INNER JOIN sys_user_role ur ON r.id = ur.role_id WHERE ur.user_id = #{userId} AND r.status = 1")
    List<String> findRolesByUserId(Long userId);

    @Select("SELECT m.perms FROM sys_menu m INNER JOIN sys_role_menu rm ON m.id = rm.menu_id INNER JOIN sys_user_role ur ON rm.role_id = ur.role_id WHERE ur.user_id = #{userId} AND m.perms IS NOT NULL AND m.perms != '' AND m.status = 1")
    List<String> findPermsByUserId(Long userId);

    class UserSecurityInfo {
        private Long id;
        private String username;
        private String password;
        private String realName;
        private Integer status;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getRealName() { return realName; }
        public void setRealName(String realName) { this.realName = realName; }
        public Integer getStatus() { return status; }
        public void setStatus(Integer status) { this.status = status; }
    }
}
