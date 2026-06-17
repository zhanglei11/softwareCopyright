package com.sursoft.sfd.admin.controller.stats;

import com.sursoft.sfd.common.core.AjaxResult;
import com.sursoft.sfd.framework.web.BaseController;
import com.sursoft.sfd.system.service.IStatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@Tag(name = "统计分析")
@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatsController extends BaseController {
    private final IStatsService statsService;

    @Operation(summary = "融合执行统计")
    @GetMapping("/fusion/summary")
    public AjaxResult<Map<String, Object>> fusionSummary(
            @RequestParam(required = false) Long schemeId,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime,
            @RequestParam(defaultValue = "day") String granularity) {
        return AjaxResult.ok(statsService.fusionSummary(schemeId, startTime, endTime, granularity));
    }

    @Operation(summary = "决策执行统计")
    @GetMapping("/decision/summary")
    public AjaxResult<Map<String, Object>> decisionSummary(
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        return AjaxResult.ok(statsService.decisionSummary(startTime, endTime));
    }
}
