package com.vqcc.system.mapper;

import com.vqcc.system.domain.QualityDetectionRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface QualityDetectionRecordMapper {
    List<QualityDetectionRecord> selectByTaskId(@Param("taskId") Long taskId,
                                                 @Param("isQualified") Integer isQualified);
    QualityDetectionRecord selectById(Long id);
    int insert(QualityDetectionRecord record);
    long countByTaskId(Long taskId);
    long countQualifiedByTaskId(Long taskId);
}
