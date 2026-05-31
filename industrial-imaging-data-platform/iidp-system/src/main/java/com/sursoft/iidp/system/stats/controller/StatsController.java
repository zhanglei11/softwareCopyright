package com.sursoft.iidp.system.stats.controller;

import com.sursoft.iidp.common.core.AjaxResult;
import com.sursoft.iidp.system.stats.dto.OverviewDTO;
import com.sursoft.iidp.system.stats.service.StatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@Tag(name = "统计分析")
@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatsController {
    private final StatsService statsService;

    @Operation(summary = "平台概览数据")
    @PreAuthorize("hasAuthority('stats:overview:view')")
    @GetMapping("/overview")
    public AjaxResult<OverviewDTO> overview() {
        return AjaxResult.success(statsService.getOverview());
    }

    @Operation(summary = "接入量趋势")
    @PreAuthorize("hasAuthority('stats:analysis:view')")
    @GetMapping("/ingest/trend")
    public AjaxResult<List<Map<String, Object>>> ingestTrend(
            @RequestParam(required = false, defaultValue = "7") String days) {
        return AjaxResult.success(statsService.getIngestTrend(days));
    }

    @Operation(summary = "处理任务汇总")
    @PreAuthorize("hasAuthority('stats:process:view')")
    @GetMapping("/process/summary")
    public AjaxResult<Map<String, Object>> processSummary() {
        return AjaxResult.success(statsService.getProcessSummary());
    }

    @Operation(summary = "接入状态分析")
    @PreAuthorize("hasAuthority('stats:analysis:view')")
    @GetMapping("/ingest/analysis")
    public AjaxResult<List<Map<String, Object>>> ingestAnalysis() {
        return AjaxResult.success(statsService.getIngestAnalysis());
    }

    @Operation(summary = "数据源贡献度")
    @PreAuthorize("hasAuthority('stats:overview:view')")
    @GetMapping("/datasource/contribution")
    public AjaxResult<List<Map<String, Object>>> datasourceContribution() {
        return AjaxResult.success(statsService.getDatasourceContribution());
    }

    @Operation(summary = "文件类型分布")
    @PreAuthorize("hasAuthority('stats:overview:view')")
    @GetMapping("/filetype/distribution")
    public AjaxResult<List<Map<String, Object>>> fileTypeDistribution() {
        return AjaxResult.success(statsService.getFileTypeDistribution());
    }
}
