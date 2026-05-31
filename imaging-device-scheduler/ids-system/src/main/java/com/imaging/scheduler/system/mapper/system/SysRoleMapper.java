package com.imaging.scheduler.system.mapper.system;

import com.imaging.scheduler.system.domain.system.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysRoleMapper {
    List<SysRole> selectList();
    SysRole selectById(@Param("id") Long id);
    int insert(SysRole role);
    int update(SysRole role);
    int deleteById(@Param("id") Long id);
    int countUsersByRoleId(@Param("roleId") Long roleId);
    void deleteUserRoleByRoleId(@Param("roleId") Long roleId);
    void deleteMenuRoleByRoleId(@Param("roleId") Long roleId);
    void insertRoleMenus(@Param("roleId") Long roleId, @Param("menuIds") List<Long> menuIds);
    void deleteUserRoleByUserId(@Param("userId") Long userId);
    void insertUserRoles(@Param("userId") Long userId, @Param("roleIds") List<Long> roleIds);
    List<Long> selectMenuIdsByRoleId(@Param("roleId") Long roleId);
}
