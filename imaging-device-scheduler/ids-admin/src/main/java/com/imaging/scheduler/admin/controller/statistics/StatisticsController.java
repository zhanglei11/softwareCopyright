package com.imaging.scheduler.admin.controller.statistics;

import com.imaging.scheduler.common.core.AjaxResult;
import com.imaging.scheduler.system.service.dispatch.StatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "统计分析")
@RestController
@RequestMapping("/api/v1/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @Operation(summary = "设备状态分布统计")
    @PreAuthorize("hasAuthority('statistics:view')")
    @GetMapping("/device/status")
    public AjaxResult<Map<String, Object>> deviceStatusStat() {
        return AjaxResult.success(statisticsService.getDeviceStatusStat());
    }

    @Operation(summary = "设备利用率趋势（近N天）")
    @PreAuthorize("hasAuthority('statistics:view')")
    @GetMapping("/device/trend")
    public AjaxResult<Map<String, Object>> deviceTrend(
            @RequestParam(name = "days", defaultValue = "7") int days) {
        return AjaxResult.success(statisticsService.getDeviceTrend(days));
    }

    @Operation(summary = "任务状态分布统计")
    @PreAuthorize("hasAuthority('statistics:view')")
    @GetMapping("/task/status")
    public AjaxResult<Map<String, Object>> taskStatusStat() {
        return AjaxResult.success(statisticsService.getTaskStatusStat());
    }

    @Operation(summary = "任务趋势（近N天）")
    @PreAuthorize("hasAuthority('statistics:view')")
    @GetMapping("/task/trend")
    public AjaxResult<Map<String, Object>> taskTrend(
            @RequestParam(name = "days", defaultValue = "7") int days) {
        return AjaxResult.success(statisticsService.getTaskTrend(days));
    }

    @Operation(summary = "按场景统计任务数")
    @PreAuthorize("hasAuthority('statistics:view')")
    @GetMapping("/task/by-scene")
    public AjaxResult<Map<String, Object>> taskByScene() {
        return AjaxResult.success(statisticsService.getTaskByScene());
    }

    @Operation(summary = "设备故障统计")
    @PreAuthorize("hasAuthority('statistics:view')")
    @GetMapping("/device/fault")
    public AjaxResult<Map<String, Object>> deviceFaultStat() {
        return AjaxResult.success(statisticsService.getDeviceFaultStat());
    }
}
