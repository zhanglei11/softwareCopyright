package com.sursoft.vision.system.mapper;

import com.sursoft.vision.system.domain.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface SysUserMapper {
    SysUser selectById(Long id);
    SysUser selectByUsername(String username);
    List<SysUser> selectList(@Param("username") String username,
                              @Param("realName") String realName,
                              @Param("status") Integer status);
    int insert(SysUser user);
    int updateById(SysUser user);
    int deleteById(Long id);
    List<String> selectRoleKeysByUserId(Long userId);
    List<String> selectPermsByUserId(Long userId);
    int insertUserRoles(@Param("userId") Long userId, @Param("roleIds") List<Long> roleIds);
    int deleteUserRoles(Long userId);
}
