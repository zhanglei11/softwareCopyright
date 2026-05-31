package com.sursoft.sfd.system.mapper;

import com.sursoft.sfd.system.domain.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface SysUserMapper {
    List<SysUser> selectList(@Param("username") String username,
                              @Param("realName") String realName,
                              @Param("status") Integer status);
    SysUser selectById(@Param("id") Long id);
    SysUser selectByUsername(@Param("username") String username);
    int insert(SysUser user);
    int update(SysUser user);
    int deleteById(@Param("id") Long id);
    int updateStatus(@Param("id") Long id, @Param("status") Integer status,
                     @Param("updatedBy") Long updatedBy);
    int resetPassword(@Param("id") Long id, @Param("password") String password,
                      @Param("updatedBy") Long updatedBy);
    void deleteUserRoles(@Param("userId") Long userId);
    void insertUserRoles(@Param("userId") Long userId, @Param("roleIds") List<Long> roleIds);
    List<String> selectRoleCodesByUserId(@Param("userId") Long userId);
    List<String> selectPermissionsByUserId(@Param("userId") Long userId);
}
