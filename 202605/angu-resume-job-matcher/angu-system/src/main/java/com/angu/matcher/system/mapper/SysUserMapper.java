package com.angu.matcher.system.mapper;

import com.angu.matcher.system.domain.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysUserMapper {
    List<SysUser> selectList(@Param("username") String username,
                              @Param("phone") String phone,
                              @Param("status") Integer status);
    SysUser selectById(Long id);
    SysUser selectByUsername(String username);
    int insert(SysUser user);
    int update(SysUser user);
    int deleteById(Long id);
    List<Long> selectRoleIdsByUserId(Long userId);
    int deleteUserRoles(Long userId);
    int insertUserRoles(@Param("userId") Long userId, @Param("roleIds") List<Long> roleIds);
    int updateErrorCount(@Param("id") Long id, @Param("errorCount") int errorCount,
                          @Param("lockedUntil") java.time.LocalDateTime lockedUntil);
}
