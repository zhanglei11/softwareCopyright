package com.vqcc.system.mapper;

import com.vqcc.system.domain.QualityTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface QualityTaskMapper {
    List<QualityTask> selectList(@Param("taskName") String taskName,
                                  @Param("detectionTarget") String detectionTarget,
                                  @Param("status") Integer status,
                                  @Param("templateId") Long templateId);
    QualityTask selectById(Long id);
    int insert(QualityTask task);
    int update(QualityTask task);
    int deleteById(Long id);
    String selectMaxCodeByDate(String dateStr);
    int updateStatistics(@Param("taskId") Long taskId,
                         @Param("qualifiedCount") int qualifiedCount,
                         @Param("unqualifiedCount") int unqualifiedCount);
}
