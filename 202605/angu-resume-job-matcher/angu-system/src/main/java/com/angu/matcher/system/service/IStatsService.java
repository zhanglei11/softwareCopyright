package com.angu.matcher.system.service;

import java.util.Map;

public interface IStatsService {
    Map<String, Object> getDashboard();
    Map<String, Object> getSourceStats(String startDate, String endDate);
}
