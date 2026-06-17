package com.angu.matcher.admin.controller;

import com.angu.matcher.common.result.AjaxResult;
import com.angu.matcher.framework.web.BaseController;
import com.angu.matcher.system.service.IStatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "统计报表")
@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatsController extends BaseController {

    private final IStatsService statsService;

    @Operation(summary = "数据看板")
    @PreAuthorize("hasAuthority('stats:dashboard:view')")
    @GetMapping("/dashboard")
    public AjaxResult<?> dashboard() {
        return AjaxResult.success(statsService.getDashboard());
    }

    @Operation(summary = "来源统计")
    @PreAuthorize("hasAuthority('stats:report:view')")
    @GetMapping("/source")
    public AjaxResult<?> source(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        return AjaxResult.success(statsService.getSourceStats(startDate, endDate));
    }
}
