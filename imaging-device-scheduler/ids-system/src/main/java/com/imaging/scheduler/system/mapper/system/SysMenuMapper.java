package com.imaging.scheduler.system.mapper.system;

import com.imaging.scheduler.system.domain.system.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysMenuMapper {
    List<SysMenu> selectAll();
    SysMenu selectById(@Param("id") Long id);
    int insert(SysMenu menu);
    int update(SysMenu menu);
    int deleteById(@Param("id") Long id);
    int countChildByParentId(@Param("parentId") Long parentId);
    List<SysMenu> selectMenusByRoleIds(@Param("roleIds") List<Long> roleIds);
    List<String> selectPermsByUserId(@Param("userId") Long userId);
}
