package com.angu.ai.system.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Schema(description = "使用量看板数据")
public class DashboardVO {
    @Schema(description = "今日调用总次数")
    private Long todayCallCount;
    @Schema(description = "本月累计调用次数")
    private Long monthCallCount;
    @Schema(description = "本月活跃用户数")
    private Long monthActiveUsers;
    @Schema(description = "近30天每日调用趋势")
    private List<Map<String, Object>> dailyTrend;
    @Schema(description = "各场景调用量分布 Top10")
    private List<Map<String, Object>> sceneDistribution;
    @Schema(description = "各模型调用占比")
    private List<Map<String, Object>> modelDistribution;
    @Schema(description = "Token 消耗趋势")
    private List<Map<String, Object>> tokenTrend;
}
