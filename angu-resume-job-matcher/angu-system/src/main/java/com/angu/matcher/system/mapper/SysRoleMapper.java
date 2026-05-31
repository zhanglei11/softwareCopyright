package com.angu.matcher.system.mapper;

import com.angu.matcher.system.domain.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysRoleMapper {
    List<SysRole> selectAll();
    SysRole selectById(Long id);
    SysRole selectByRoleCode(String roleCode);
    int insert(SysRole role);
    int update(SysRole role);
    int deleteById(Long id);
    int countUserByRoleId(Long roleId);
    List<Long> selectMenuIdsByRoleId(Long roleId);
    int deleteRoleMenus(Long roleId);
    int insertRoleMenus(@Param("roleId") Long roleId,
                         @Param("menuIds") java.util.List<Long> menuIds);
    List<String> selectPermCodesByUserId(Long userId);
    List<SysRole> selectRolesByUserId(Long userId);
}
