package com.vqcc.system.mapper;

import com.vqcc.system.domain.QualityTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface QualityTemplateMapper {
    List<QualityTemplate> selectList(@Param("templateName") String templateName,
                                      @Param("status") Integer status);
    QualityTemplate selectById(Long id);
    int insert(QualityTemplate template);
    int update(QualityTemplate template);
    int deleteById(Long id);
    int insertTemplateMetrics(@Param("templateId") Long templateId, @Param("metricIds") List<Long> metricIds);
    int deleteTemplateMetrics(Long templateId);
    String selectMaxCode();
}
