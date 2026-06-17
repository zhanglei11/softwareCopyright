package com.sursoft.vision.admin.controller.stats;

import com.sursoft.vision.common.core.AjaxResult;
import com.sursoft.vision.system.service.StatsService;
import com.sursoft.vision.system.vo.DashboardVO;
import com.sursoft.vision.system.vo.TrendVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/stats")
@Tag(name = "统计报表")
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @GetMapping("/dashboard")
    @Operation(summary = "缺陷统计看板", description = "返回今日核心指标及图表数据")
    public AjaxResult<DashboardVO> dashboard(@RequestParam(value = "date", required = false) String date) {
        return AjaxResult.success(statsService.dashboard(date));
    }

    @GetMapping("/trend")
    @Operation(summary = "趋势分析", description = "按时间/产线/缺陷类型多维趋势")
    public AjaxResult<TrendVO> trend(
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate,
            @RequestParam(value = "granularity", defaultValue = "day") String granularity,
            @RequestParam(value = "lineId", required = false) Long lineId,
            @RequestParam(value = "categoryId", required = false) Long categoryId) {
        return AjaxResult.success(statsService.trend(startDate, endDate, granularity, lineId, categoryId));
    }
}
