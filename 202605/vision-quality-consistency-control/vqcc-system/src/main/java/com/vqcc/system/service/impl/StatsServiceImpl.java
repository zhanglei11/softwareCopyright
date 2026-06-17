package com.vqcc.system.service.impl;

import com.vqcc.system.mapper.QualityDefectMapper;
import com.vqcc.system.mapper.QualityDetectionRecordMapper;
import com.vqcc.system.mapper.QualityTaskMapper;
import com.vqcc.system.service.IStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements IStatsService {

    private final QualityTaskMapper taskMapper;
    private final QualityDefectMapper defectMapper;
    private final QualityDetectionRecordMapper recordMapper;

    @Override
    public Map<String, Object> dashboard() {
        Map<String, Object> result = new HashMap<>();
        result.put("defectSummary", defectMapper.selectSummaryStats());
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        result.put("defectTrend", defectMapper.selectTrendStats(
                LocalDate.now().minusDays(30).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), today));
        return result;
    }

    @Override
    public Map<String, Object> qualityTrend(String startDate, String endDate) {
        Map<String, Object> result = new HashMap<>();
        result.put("trend", defectMapper.selectTrendStats(startDate, endDate));
        return result;
    }

    @Override
    public Map<String, Object> taskAnalysis(Long taskId) {
        Map<String, Object> result = new HashMap<>();
        long total = recordMapper.countByTaskId(taskId);
        long qualified = recordMapper.countQualifiedByTaskId(taskId);
        result.put("totalCount", total);
        result.put("qualifiedCount", qualified);
        result.put("unqualifiedCount", total - qualified);
        result.put("qualifiedRate", total > 0 ? Math.round(qualified * 10000.0 / total) / 100.0 : 0);
        result.put("defects", defectMapper.selectList(taskId, null, null, null));
        return result;
    }
}
