package com.vqcc.system.mapper;

import com.vqcc.system.domain.QualityDefectDispose;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface QualityDefectDisposeMapper {
    List<QualityDefectDispose> selectByDefectId(Long defectId);
    QualityDefectDispose selectById(Long id);
    int insert(QualityDefectDispose dispose);
    int update(QualityDefectDispose dispose);
}
