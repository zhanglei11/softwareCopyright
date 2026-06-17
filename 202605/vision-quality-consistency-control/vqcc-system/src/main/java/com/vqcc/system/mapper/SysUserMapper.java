package com.vqcc.system.mapper;

import com.vqcc.system.domain.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface SysUserMapper {
    List<SysUser> selectList(@Param("username") String username,
                              @Param("realName") String realName,
                              @Param("status") Integer status);
    SysUser selectById(Long id);
    SysUser selectByUsername(String username);
    int insert(SysUser user);
    int update(SysUser user);
    int deleteById(Long id);
    List<String> selectPermissionsByUserId(Long userId);
    List<String> selectRoleCodesByUserId(Long userId);
}
