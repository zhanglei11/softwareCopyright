package com.angu.ai.system.mapper;

import com.angu.ai.system.domain.entity.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface SysMenuMapper {
    List<SysMenu> selectAll();
    SysMenu selectById(@Param("id") Long id);
    List<SysMenu> selectByRoleId(@Param("roleId") Long roleId);
    int insert(SysMenu menu);
    int updateById(SysMenu menu);
    int deleteById(@Param("id") Long id);
    int countChildren(@Param("parentId") Long parentId);
    Set<String> selectPermsByUserId(@Param("userId") Long userId);
}
