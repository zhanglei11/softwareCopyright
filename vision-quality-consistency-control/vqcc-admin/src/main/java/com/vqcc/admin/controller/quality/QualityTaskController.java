package com.vqcc.admin.controller.quality;

import com.vqcc.common.result.AjaxResult;
import com.vqcc.common.result.TableDataInfo;
import com.vqcc.framework.security.LoginUser;
import com.vqcc.system.domain.QualityDetectionRecord;
import com.vqcc.system.domain.QualityTask;
import com.vqcc.system.dto.request.DetectionRecordSubmitReq;
import com.vqcc.system.dto.request.TaskCreateReq;
import com.vqcc.system.service.IQualityTaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "检测任务管理")
@RestController
@RequestMapping("/api/v1/quality/tasks")
@RequiredArgsConstructor
public class QualityTaskController {

    private final IQualityTaskService taskService;

    @Operation(summary = "任务列表")
    @PreAuthorize("hasAuthority('quality:task:list')")
    @GetMapping
    public AjaxResult<TableDataInfo<QualityTask>> list(
            @RequestParam(required = false) String taskName,
            @RequestParam(required = false) String detectionTarget,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Long templateId) {
        return AjaxResult.ok(TableDataInfo.ok(taskService.list(taskName, detectionTarget, status, templateId)));
    }

    @Operation(summary = "任务详情")
    @PreAuthorize("hasAuthority('quality:task:list')")
    @GetMapping("/{id}")
    public AjaxResult<QualityTask> getById(@PathVariable Long id) {
        return AjaxResult.ok(taskService.getById(id));
    }

    @Operation(summary = "创建任务")
    @PreAuthorize("hasAuthority('quality:task:add')")
    @PostMapping
    public AjaxResult<Void> create(@Valid @RequestBody TaskCreateReq req,
                                    @AuthenticationPrincipal LoginUser loginUser) {
        taskService.create(req, loginUser.getUserId());
        return AjaxResult.ok(null);
    }

    @Operation(summary = "开始任务")
    @PreAuthorize("hasAuthority('quality:task:edit')")
    @PostMapping("/{id}/start")
    public AjaxResult<Void> start(@PathVariable Long id, @AuthenticationPrincipal LoginUser loginUser) {
        taskService.startTask(id, loginUser.getUserId());
        return AjaxResult.ok(null);
    }

    @Operation(summary = "完成任务")
    @PreAuthorize("hasAuthority('quality:task:edit')")
    @PostMapping("/{id}/complete")
    public AjaxResult<Void> complete(@PathVariable Long id, @AuthenticationPrincipal LoginUser loginUser) {
        taskService.completeTask(id, loginUser.getUserId());
        return AjaxResult.ok(null);
    }

    @Operation(summary = "取消任务")
    @PreAuthorize("hasAuthority('quality:task:edit')")
    @PostMapping("/{id}/cancel")
    public AjaxResult<Void> cancel(@PathVariable Long id, @AuthenticationPrincipal LoginUser loginUser) {
        taskService.cancelTask(id, loginUser.getUserId());
        return AjaxResult.ok(null);
    }

    @Operation(summary = "提交检测记录")
    @PreAuthorize("hasAuthority('quality:task:edit')")
    @PostMapping("/records")
    public AjaxResult<QualityDetectionRecord> submitRecord(
            @RequestBody DetectionRecordSubmitReq req,
            @AuthenticationPrincipal LoginUser loginUser) {
        return AjaxResult.ok(taskService.submitRecord(req, loginUser.getUserId()));
    }

    @Operation(summary = "查看任务检测记录")
    @PreAuthorize("hasAuthority('quality:task:list')")
    @GetMapping("/{id}/records")
    public AjaxResult<List<QualityDetectionRecord>> getRecords(
            @PathVariable Long id,
            @RequestParam(required = false) Integer isQualified) {
        return AjaxResult.ok(taskService.getRecords(id, isQualified));
    }
}
