package com.sursoft.iidp.system.stats.service;

import com.sursoft.iidp.system.stats.dto.OverviewDTO;
import java.util.List;
import java.util.Map;

public interface StatsService {
    OverviewDTO getOverview();
    List<Map<String, Object>> getIngestTrend(String days);
    Map<String, Object> getProcessSummary();
    List<Map<String, Object>> getIngestAnalysis();
    List<Map<String, Object>> getDatasourceContribution();
    List<Map<String, Object>> getFileTypeDistribution();
}
