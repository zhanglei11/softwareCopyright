package com.sursoft.iidp.system.storage.mapper;

import com.sursoft.iidp.system.storage.domain.StorageCleanRule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface StorageCleanRuleMapper {
    List<StorageCleanRule> selectList(StorageCleanRule query);
    StorageCleanRule selectById(@Param("id") Long id);
    int insert(StorageCleanRule rule);
    int update(StorageCleanRule rule);
    int deleteById(@Param("id") Long id);
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
}
