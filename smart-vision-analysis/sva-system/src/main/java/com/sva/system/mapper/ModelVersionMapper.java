package com.sva.system.mapper;

import com.sva.system.domain.ModelVersion;
import com.sva.system.domain.RecognitionTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ModelVersionMapper {
    List<ModelVersion> selectList(@Param("modelName") String modelName, @Param("status") Integer status);
    ModelVersion selectById(@Param("id") Long id);
    int insert(ModelVersion version);
    int update(ModelVersion version);
    int deleteById(@Param("id") Long id);
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
    long countTasksByModelId(@Param("modelId") Long modelId);
    List<RecognitionTask> selectTasksByModelId(@Param("modelId") Long modelId);
}
