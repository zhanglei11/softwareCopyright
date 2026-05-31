package com.sursoft.iidp.system.ingest.mapper;

import com.sursoft.iidp.system.ingest.domain.IngestRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface IngestRecordMapper {
    List<IngestRecord> selectList(@Param("taskId") Long taskId, @Param("status") String status);
    int insert(IngestRecord record);
    String selectMaxCode();
    long countToday();
    long sumTodaySize();
    List<java.util.Map<String, Object>> selectIngestTrend(@Param("days") int days);
    List<java.util.Map<String, Object>> selectStatusAnalysis();
    List<java.util.Map<String, Object>> selectDatasourceContribution();
}
