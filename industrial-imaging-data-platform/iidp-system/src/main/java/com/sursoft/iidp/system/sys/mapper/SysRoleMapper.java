package com.sursoft.iidp.system.sys.mapper;

import com.sursoft.iidp.system.sys.domain.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface SysRoleMapper {
    List<SysRole> selectList(SysRole query);
    SysRole selectById(@Param("id") Long id);
    int insert(SysRole role);
    int update(SysRole role);
    int deleteById(@Param("id") Long id);
    void deleteRoleMenuByRoleId(@Param("roleId") Long roleId);
    void insertRoleMenuBatch(@Param("roleId") Long roleId, @Param("menuIds") List<Long> menuIds);
    List<Long> selectMenuIdsByRoleId(@Param("roleId") Long roleId);
    List<SysRole> selectRolesByUserId(@Param("userId") Long userId);
}
