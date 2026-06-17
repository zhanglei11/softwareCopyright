package com.vqcc.system.mapper;

import com.vqcc.system.domain.QualityDefect;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

@Mapper
public interface QualityDefectMapper {
    List<QualityDefect> selectList(@Param("taskId") Long taskId,
                                    @Param("disposeStatus") Integer disposeStatus,
                                    @Param("verifyStatus") Integer verifyStatus,
                                    @Param("imageId") String imageId);
    QualityDefect selectById(Long id);
    int insert(QualityDefect defect);
    int update(QualityDefect defect);
    int deleteById(Long id);
    String selectMaxCodeByDate(String dateStr);
    List<Map<String, Object>> selectTrendStats(@Param("startDate") String startDate,
                                                @Param("endDate") String endDate);
    Map<String, Object> selectSummaryStats();
}
