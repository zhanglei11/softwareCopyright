package com.sva.system.mapper;

import com.sva.system.domain.SysMenu;
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
    long countChildrenByParentId(@Param("parentId") Long parentId);
}
