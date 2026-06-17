package com.sursoft.sfd.system.service.impl;

import com.sursoft.sfd.system.mapper.DecisionResultMapper;
import com.sursoft.sfd.system.mapper.FusionResultMapper;
import com.sursoft.sfd.system.service.IStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements IStatsService {
    private final FusionResultMapper fusionResultMapper;
    private final DecisionResultMapper decisionResultMapper;

    @Override
    public Map<String, Object> fusionSummary(Long schemeId, String startTime, String endTime, String granularity) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("total", fusionResultMapper.countByDateRange(schemeId, startTime, endTime));
        result.put("trend", fusionResultMapper.countTrendByDay(schemeId, startTime, endTime));
        result.put("bySceneType", fusionResultMapper.countBySceneType(startTime, endTime));
        return result;
    }

    @Override
    public Map<String, Object> decisionSummary(String startTime, String endTime) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("total", decisionResultMapper.countByDateRange(startTime, endTime));
        result.put("byRule", decisionResultMapper.countByRule(startTime, endTime));
        result.put("avgResponseTime", decisionResultMapper.avgResponseTime(startTime, endTime));
        return result;
    }
}
