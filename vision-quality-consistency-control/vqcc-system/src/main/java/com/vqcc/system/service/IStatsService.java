package com.vqcc.system.service;

import java.util.Map;

public interface IStatsService {
    Map<String, Object> dashboard();
    Map<String, Object> qualityTrend(String startDate, String endDate);
    Map<String, Object> taskAnalysis(Long taskId);
}
