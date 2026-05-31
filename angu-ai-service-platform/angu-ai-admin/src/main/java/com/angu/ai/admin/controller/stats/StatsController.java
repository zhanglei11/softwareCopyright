package com.angu.ai.admin.controller.stats;

import com.angu.ai.common.core.domain.AjaxResult;
import com.angu.ai.system.domain.query.StatsQuery;
import com.angu.ai.system.domain.vo.DashboardVO;
import com.angu.ai.system.service.IStatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@Tag(name = "数据统计")
@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatsController {
    private final IStatsService statsService;

    @Operation(summary = "仪表板汇总")
    @GetMapping("/dashboard")
    @PreAuthorize("hasAuthority('stats:dashboard:view')")
    public AjaxResult<DashboardVO> dashboard() { return AjaxResult.success(statsService.getDashboard()); }

    @Operation(summary = "场景调用统计")
    @GetMapping("/scenes")
    @PreAuthorize("hasAuthority('stats:scene:view')")
    public AjaxResult<List<Map<String, Object>>> sceneStats(StatsQuery query) { return AjaxResult.success(statsService.getSceneStats(query)); }

    @Operation(summary = "用户使用排行")
    @GetMapping("/user-rank")
    @PreAuthorize("hasAuthority('stats:user:view')")
    public AjaxResult<List<Map<String, Object>>> userRank() { return AjaxResult.success(statsService.getUserRank()); }
}
