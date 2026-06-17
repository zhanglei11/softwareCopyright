package com.angu.ai.system.service.impl;

import com.angu.ai.system.domain.query.StatsQuery;
import com.angu.ai.system.domain.vo.DashboardVO;
import com.angu.ai.system.mapper.AiCallLogMapper;
import com.angu.ai.system.service.IStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements IStatsService {
    private final AiCallLogMapper callLogMapper;

    @Override
    public DashboardVO getDashboard() {
        DashboardVO vo = new DashboardVO();
        vo.setTodayCallCount(callLogMapper.countToday());
        vo.setMonthCallCount(callLogMapper.countThisMonth());
        vo.setMonthActiveUsers(callLogMapper.countActiveUsersThisMonth());
        vo.setDailyTrend(callLogMapper.dailyTrend(30));
        vo.setSceneDistribution(callLogMapper.sceneDistributionTop10());
        vo.setModelDistribution(callLogMapper.modelDistribution());
        vo.setTokenTrend(callLogMapper.tokenTrend(30));
        return vo;
    }

    @Override
    public List<Map<String, Object>> getSceneStats(StatsQuery query) {
        return callLogMapper.sceneStats(query.getStartDate(), query.getEndDate());
    }

    @Override
    public List<Map<String, Object>> getUserRank() {
        return callLogMapper.userRankTop20();
    }
}
