package com.sursoft.iidp.system.sys.mapper;

import com.sursoft.iidp.system.sys.domain.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface SysUserMapper {
    SysUser selectByUsername(@Param("username") String username);
    SysUser selectById(@Param("id") Long id);
    List<SysUser> selectList(SysUser query);
    int insert(SysUser user);
    int update(SysUser user);
    int deleteById(@Param("id") Long id);
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
    int checkUsernameUnique(@Param("username") String username, @Param("excludeId") Long excludeId);
}
