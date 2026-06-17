package com.angu.ai.system.mapper;

import com.angu.ai.system.domain.entity.AiSceneCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AiSceneCategoryMapper {
    List<AiSceneCategory> selectAll();
    AiSceneCategory selectById(@Param("id") Long id);
    int insert(AiSceneCategory category);
    int updateById(AiSceneCategory category);
    int deleteById(@Param("id") Long id);
    int countScenes(@Param("categoryId") Long categoryId);
}
