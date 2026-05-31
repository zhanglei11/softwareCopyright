package com.sursoft.vision.system.mapper;

import com.sursoft.vision.system.domain.DefectRecord;
import com.sursoft.vision.system.query.DefectRecordQuery;
import com.sursoft.vision.system.vo.DefectRecordVO;
import com.sursoft.vision.system.vo.BatchTraceVO;
import com.sursoft.vision.system.vo.NameValueVO;
import com.sursoft.vision.system.vo.DashboardVO;
import com.sursoft.vision.system.vo.TrendVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface DefectRecordMapper {
    DefectRecord selectById(Long id);
    DefectRecordVO selectVoById(Long id);
    List<DefectRecordVO> selectList(DefectRecordQuery query);
    List<DefectRecordVO> selectByBatchNo(@Param("batchNo") String batchNo, @Param("lineId") Long lineId);
    List<DefectRecordVO> selectBySerialNo(String serialNo);
    int insert(DefectRecord record);
    int updateDisposeStatus(DefectRecord record);
    // Dashboard stats
    Long countTodayTotal(@Param("date") String date);
    Long countTodayDefect(@Param("date") String date);
    Long countTodayQualified(@Param("date") String date);
    List<DashboardVO.TrendItem> selectMonthTrend(@Param("startDate") String startDate);
    List<NameValueVO> selectCategoryDistribution(@Param("startDate") String startDate, @Param("endDate") String endDate);
    List<DashboardVO.LineCompareVO> selectLineComparison(@Param("date") String date);
    // Trend
    List<TrendVO.TrendItem> selectTrend(@Param("startDate") String startDate, @Param("endDate") String endDate,
                                         @Param("granularity") String granularity,
                                         @Param("lineId") Long lineId,
                                         @Param("categoryId") Long categoryId);
    // Batch trace stats
    Long countBatchTotal(@Param("batchNo") String batchNo, @Param("lineId") Long lineId);
    Long countBatchQualified(@Param("batchNo") String batchNo, @Param("lineId") Long lineId);
    List<BatchTraceVO.CategoryDistVO> selectBatchCategoryDist(@Param("batchNo") String batchNo, @Param("lineId") Long lineId);
}
