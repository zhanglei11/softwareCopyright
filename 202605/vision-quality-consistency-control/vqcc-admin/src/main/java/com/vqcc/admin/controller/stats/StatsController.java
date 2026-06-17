package com.vqcc.admin.controller.stats;

import com.vqcc.common.result.AjaxResult;
import com.vqcc.system.service.IStatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@Tag(name = "统计分析")
@RestController
@RequestMapping("/api/v1/stats")
@RequiredArgsConstructor
public class StatsController {

    private final IStatsService statsService;

    @Operation(summary = "看板数据")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/dashboard")
    public AjaxResult<Map<String, Object>> dashboard() {
        return AjaxResult.ok(statsService.dashboard());
    }

    @Operation(summary = "质量趋势")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/trend")
    public AjaxResult<Map<String, Object>> trend(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        return AjaxResult.ok(statsService.qualityTrend(startDate, endDate));
    }
}
