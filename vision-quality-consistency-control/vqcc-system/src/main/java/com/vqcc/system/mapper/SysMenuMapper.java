package com.vqcc.system.mapper;

import com.vqcc.system.domain.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface SysMenuMapper {
    List<SysMenu> selectAll();
    List<SysMenu> selectByUserId(Long userId);
    SysMenu selectById(Long id);
    int insert(SysMenu menu);
    int update(SysMenu menu);
    int deleteById(Long id);
}
