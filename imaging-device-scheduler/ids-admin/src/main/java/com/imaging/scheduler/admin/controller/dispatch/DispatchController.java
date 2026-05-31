package com.imaging.scheduler.admin.controller.dispatch;

import com.imaging.scheduler.common.core.AjaxResult;
import com.imaging.scheduler.common.core.TableDataInfo;
import com.imaging.scheduler.system.domain.dispatch.DispatchConfig;
import com.imaging.scheduler.system.domain.dispatch.DispatchLog;
import com.imaging.scheduler.system.dto.req.DispatchConfigReq;
import com.imaging.scheduler.system.dto.req.DispatchLogQueryReq;
import com.imaging.scheduler.system.service.dispatch.DispatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "调度管理", description = "任务调度、设备甘特图、配置管理")
@RestController
@RequestMapping("/api/v1/dispatch")
@RequiredArgsConstructor
public class DispatchController {

    private final DispatchService dispatchService;

    @Operation(summary = "调度总览（今日统计）")
    @PreAuthorize("hasAuthority('dispatch:overview:view')")
    @GetMapping("/overview")
    public AjaxResult<Map<String, Object>> overview() {
        return AjaxResult.success(dispatchService.getOverview());
    }

    @Operation(summary = "甘特图数据")
    @PreAuthorize("hasAuthority('dispatch:gantt:view')")
    @GetMapping("/gantt")
    public AjaxResult<List<Map<String, Object>>> gantt(
            @RequestParam(name = "sceneId", required = false) Long sceneId,
            @RequestParam(name = "date", required = false) String date) {
        return AjaxResult.success(dispatchService.getGanttData(sceneId, date));
    }

    @Operation(summary = "超时预警任务")
    @PreAuthorize("hasAuthority('dispatch:alert:view')")
    @GetMapping("/alerts/timeout")
    public AjaxResult<List<Map<String, Object>>> timeoutAlerts() {
        return AjaxResult.success(dispatchService.getTimeoutAlerts());
    }

    @Operation(summary = "获取调度配置")
    @PreAuthorize("hasAuthority('dispatch:config:view')")
    @GetMapping("/config")
    public AjaxResult<DispatchConfig> getConfig() {
        return AjaxResult.success(dispatchService.getConfig());
    }

    @Operation(summary = "更新调度配置")
    @PreAuthorize("hasAuthority('dispatch:config:edit')")
    @PutMapping("/config")
    public AjaxResult<Void> updateConfig(@RequestBody DispatchConfigReq req) {
        dispatchService.updateConfig(req);
        return AjaxResult.success();
    }

    @Operation(summary = "调度日志分页")
    @PreAuthorize("hasAuthority('dispatch:log:list')")
    @GetMapping("/logs")
    public TableDataInfo<DispatchLog> logs(DispatchLogQueryReq req) {
        return dispatchService.getLogList(req);
    }

    @Operation(summary = "导出调度日志")
    @PreAuthorize("hasAuthority('dispatch:log:export')")
    @GetMapping("/logs/export")
    public AjaxResult<List<DispatchLog>> exportLogs(DispatchLogQueryReq req) {
        return AjaxResult.success(dispatchService.exportLogs(req));
    }
}
