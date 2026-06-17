package com.sva.system.mapper;

import com.sva.system.domain.RecognitionTask;
import com.sva.system.query.TaskQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface RecognitionTaskMapper {
    List<RecognitionTask> selectList(TaskQuery query);
    RecognitionTask selectById(@Param("id") Long id);
    int insert(RecognitionTask task);
    int update(RecognitionTask task);
    int deleteById(@Param("id") Long id);
    void insertTaskImageRels(@Param("taskId") Long taskId, @Param("imageIds") List<Long> imageIds);
    List<Long> selectImageIdsByTaskId(@Param("taskId") Long taskId);
}
