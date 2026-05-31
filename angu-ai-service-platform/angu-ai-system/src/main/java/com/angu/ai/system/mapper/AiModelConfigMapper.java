package com.angu.ai.system.mapper;

import com.angu.ai.system.domain.entity.AiModelConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AiModelConfigMapper {
    List<AiModelConfig> selectAll();
    AiModelConfig selectById(@Param("id") Long id);
    int insert(AiModelConfig config);
    int updateById(AiModelConfig config);
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
}
