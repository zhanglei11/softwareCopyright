package com.vqcc.system.mapper;

import com.vqcc.system.domain.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface SysRoleMapper {
    List<SysRole> selectList(@Param("roleName") String roleName, @Param("status") Integer status);
    SysRole selectById(Long id);
    int insert(SysRole role);
    int update(SysRole role);
    int deleteById(Long id);
    int assignMenus(@Param("roleId") Long roleId, @Param("menuIds") List<Long> menuIds);
    int deleteRoleMenus(Long roleId);
    List<Long> selectMenuIdsByRoleId(Long roleId);
}
