package com.sursoft.sfd.system.mapper;

import com.sursoft.sfd.system.domain.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface SysRoleMapper {
    List<SysRole> selectList(@Param("keyword") String keyword, @Param("status") Integer status);
    SysRole selectById(@Param("id") Long id);
    int insert(SysRole role);
    int update(SysRole role);
    int deleteById(@Param("id") Long id);
    int countUsersByRoleId(@Param("roleId") Long roleId);
    void deleteRoleMenus(@Param("roleId") Long roleId);
    void insertRoleMenus(@Param("roleId") Long roleId, @Param("menuIds") List<Long> menuIds);
    List<Long> selectMenuIdsByRoleId(@Param("roleId") Long roleId);
}
