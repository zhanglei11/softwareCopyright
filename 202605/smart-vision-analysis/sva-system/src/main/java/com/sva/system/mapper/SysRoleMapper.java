package com.sva.system.mapper;

import com.sva.system.domain.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface SysRoleMapper {
    List<SysRole> selectList(@Param("roleName") String roleName, @Param("status") Integer status);
    SysRole selectById(@Param("id") Long id);
    int insert(SysRole role);
    int update(SysRole role);
    int deleteById(@Param("id") Long id);
    void insertRoleMenus(@Param("roleId") Long roleId, @Param("menuIds") List<Long> menuIds);
    void deleteRoleMenus(@Param("roleId") Long roleId);
    List<Long> selectMenuIdsByRoleId(@Param("roleId") Long roleId);
    long countUsersByRoleId(@Param("roleId") Long roleId);
}
