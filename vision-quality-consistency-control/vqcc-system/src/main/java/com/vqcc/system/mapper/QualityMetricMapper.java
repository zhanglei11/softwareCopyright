package com.vqcc.system.mapper;

import com.vqcc.system.domain.QualityMetric;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface QualityMetricMapper {
    List<QualityMetric> selectList(@Param("metricName") String metricName,
                                    @Param("metricType") Integer metricType,
                                    @Param("status") Integer status);
    QualityMetric selectById(Long id);
    List<QualityMetric> selectByTemplateId(Long templateId);
    int insert(QualityMetric metric);
    int update(QualityMetric metric);
    int deleteById(Long id);
    String selectMaxCode();
}
