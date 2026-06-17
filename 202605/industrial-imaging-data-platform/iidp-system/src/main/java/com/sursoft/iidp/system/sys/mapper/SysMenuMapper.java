package com.sursoft.iidp.system.sys.mapper;

import com.sursoft.iidp.system.sys.domain.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Set;

@Mapper
public interface SysMenuMapper {
    List<SysMenu> selectAllMenus();
    SysMenu selectById(@Param("id") Long id);
    int insert(SysMenu menu);
    int update(SysMenu menu);
    int deleteById(@Param("id") Long id);
    Set<String> selectPermsByUserId(@Param("userId") Long userId);
    List<SysMenu> selectMenuTreeByUserId(@Param("userId") Long userId);
}
