package com.vqcc.admin.controller.quality;

import com.vqcc.common.result.AjaxResult;
import com.vqcc.system.service.IStatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@Tag(name = "质量分析")
@RestController
@RequestMapping("/api/v1/quality/analysis")
@RequiredArgsConstructor
public class QualityAnalysisController {

    private final IStatsService statsService;

    @Operation(summary = "任务分析报告")
    @PreAuthorize("hasAuthority('quality:task:list')")
    @GetMapping("/tasks/{taskId}")
    public AjaxResult<Map<String, Object>> taskAnalysis(@PathVariable Long taskId) {
        return AjaxResult.ok(statsService.taskAnalysis(taskId));
    }

    @Operation(summary = "质量趋势分析")
    @PreAuthorize("hasAuthority('quality:task:list')")
    @GetMapping("/trend")
    public AjaxResult<Map<String, Object>> trend(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        return AjaxResult.ok(statsService.qualityTrend(startDate, endDate));
    }
}
