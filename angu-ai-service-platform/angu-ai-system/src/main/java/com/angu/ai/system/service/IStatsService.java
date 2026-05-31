package com.angu.ai.system.service;

import com.angu.ai.system.domain.query.StatsQuery;
import com.angu.ai.system.domain.vo.DashboardVO;

import java.util.List;
import java.util.Map;

public interface IStatsService {
    DashboardVO getDashboard();
    List<Map<String, Object>> getSceneStats(StatsQuery query);
    List<Map<String, Object>> getUserRank();
}
