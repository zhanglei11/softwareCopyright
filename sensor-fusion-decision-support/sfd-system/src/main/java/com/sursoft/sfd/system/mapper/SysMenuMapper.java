package com.sursoft.sfd.system.mapper;

import com.sursoft.sfd.system.domain.SysMenu;
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
    int countChildren(@Param("parentId") Long parentId);
    List<SysMenu> selectMenusByUserId(@Param("userId") Long userId);
}
