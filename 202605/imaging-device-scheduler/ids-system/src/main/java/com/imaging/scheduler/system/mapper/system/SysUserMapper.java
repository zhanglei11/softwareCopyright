package com.imaging.scheduler.system.mapper.system;

import com.imaging.scheduler.system.domain.system.SysUser;
import com.imaging.scheduler.system.dto.req.UserQueryReq;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysUserMapper {
    List<SysUser> selectList(UserQueryReq req);
    SysUser selectById(@Param("id") Long id);
    SysUser selectByUsername(@Param("username") String username);
    int insert(SysUser user);
    int update(SysUser user);
    int deleteById(@Param("id") Long id);
    List<String> selectRoleCodesByUserId(@Param("userId") Long userId);
    int countByUsername(@Param("username") String username, @Param("excludeId") Long excludeId);
}
