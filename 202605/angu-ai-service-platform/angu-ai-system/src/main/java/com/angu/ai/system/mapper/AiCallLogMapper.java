package com.angu.ai.system.mapper;

import com.angu.ai.system.domain.entity.AiCallLog;
import com.angu.ai.system.domain.vo.DashboardVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface AiCallLogMapper {
    int insert(AiCallLog log);
    Long countToday();
    Long countThisMonth();
    Long countActiveUsersThisMonth();
    List<Map<String, Object>> dailyTrend(@Param("days") int days);
    List<Map<String, Object>> sceneDistributionTop10();
    List<Map<String, Object>> modelDistribution();
    List<Map<String, Object>> tokenTrend(@Param("days") int days);
    List<Map<String, Object>> sceneStats(@Param("startDate") String startDate, @Param("endDate") String endDate);
    List<Map<String, Object>> userRankTop20();
    Long countTodayByUser(@Param("userId") Long userId);
    Long countThisMonthByUser(@Param("userId") Long userId);
    Long countTokenThisMonthByUser(@Param("userId") Long userId);
}
