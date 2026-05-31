package com.sursoft.vision.system.mapper;

import com.sursoft.vision.system.domain.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface SysRoleMapper {
    SysRole selectById(Long id);
    List<SysRole> selectList(@Param("roleName") String roleName, @Param("status") Integer status);
    int insert(SysRole role);
    int updateById(SysRole role);
    int deleteById(Long id);
    int insertRoleMenus(@Param("roleId") Long roleId, @Param("menuIds") List<Long> menuIds);
    int deleteRoleMenus(Long roleId);
}
