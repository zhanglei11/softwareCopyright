package com.angu.matcher.system.mapper;

import com.angu.matcher.system.domain.SysMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysMenuMapper {
    List<SysMenu> selectAll();
    SysMenu selectById(Long id);
    int countChildrenByParentId(Long parentId);
    int insert(SysMenu menu);
    int update(SysMenu menu);
    int deleteById(Long id);
    List<SysMenu> selectMenusByUserId(Long userId);
}
