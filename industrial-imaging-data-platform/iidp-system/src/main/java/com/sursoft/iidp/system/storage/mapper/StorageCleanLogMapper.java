package com.sursoft.iidp.system.storage.mapper;

import com.sursoft.iidp.system.storage.domain.StorageCleanLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface StorageCleanLogMapper {
    int insert(StorageCleanLog log);
    List<StorageCleanLog> selectList(@Param("ruleId") Long ruleId);
}
