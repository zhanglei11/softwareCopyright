package com.angu.ai.system.mapper;

import com.angu.ai.system.domain.entity.SysRole;
import com.angu.ai.system.domain.vo.RoleVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface SysRoleMapper {
    List<RoleVO> selectAll();
    RoleVO selectById(@Param("id") Long id);
    SysRole selectEntityById(@Param("id") Long id);
    int insert(SysRole role);
    int updateById(SysRole role);
    int deleteById(@Param("id") Long id);
    Set<String> selectRoleCodesByUserId(@Param("userId") Long userId);
    Set<String> selectPermsByUserId(@Param("userId") Long userId);
    int insertRoleMenu(@Param("roleId") Long roleId, @Param("menuId") Long menuId);
    int deleteRoleMenus(@Param("roleId") Long roleId);
    int insertRoleScene(@Param("roleId") Long roleId, @Param("sceneId") Long sceneId);
    int deleteRoleScenes(@Param("roleId") Long roleId);
    List<Long> selectSceneIdsByUserId(@Param("userId") Long userId);
}
