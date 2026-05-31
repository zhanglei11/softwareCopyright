package com.sursoft.vision.system.mapper;

import com.sursoft.vision.system.domain.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface SysMenuMapper {
    SysMenu selectById(Long id);
    List<SysMenu> selectAll();
    List<SysMenu> selectByRoleId(Long roleId);
    int insert(SysMenu menu);
    int updateById(SysMenu menu);
    int deleteById(Long id);
}
