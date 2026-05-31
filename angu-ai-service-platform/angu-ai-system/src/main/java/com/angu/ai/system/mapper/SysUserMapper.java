package com.angu.ai.system.mapper;

import com.angu.ai.system.domain.entity.SysUser;
import com.angu.ai.system.domain.query.UserQuery;
import com.angu.ai.system.domain.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface SysUserMapper {
    List<UserVO> selectPage(@Param("q") UserQuery query);
    UserVO selectById(@Param("id") Long id);
    UserVO selectByUsername(@Param("username") String username);
    SysUser selectEntityByUsername(@Param("username") String username);
    int insert(SysUser user);
    int updateById(SysUser user);
    int deleteById(@Param("id") Long id);
    List<Long> selectRoleIds(@Param("userId") Long userId);
    int insertUserRole(@Param("userId") Long userId, @Param("roleId") Long roleId);
    int deleteUserRoles(@Param("userId") Long userId);
    SysUser selectEntityById(@Param("id") Long id);
    Long countAll();
}
