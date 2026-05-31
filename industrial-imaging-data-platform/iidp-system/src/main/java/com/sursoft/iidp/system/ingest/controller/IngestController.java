package com.sursoft.iidp.system.ingest.controller;

import com.sursoft.iidp.common.core.AjaxResult;
import com.sursoft.iidp.common.core.PageDomain;
import com.sursoft.iidp.common.core.TableDataInfo;
import com.sursoft.iidp.framework.security.LoginUser;
import com.sursoft.iidp.framework.web.BaseController;
import com.sursoft.iidp.system.ingest.domain.IngestRecord;
import com.sursoft.iidp.system.ingest.domain.IngestTask;
import com.sursoft.iidp.system.ingest.service.IngestTaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@Tag(name = "数据接入管理")
@RestController
@RequestMapping("/api/ingest")
@RequiredArgsConstructor
public class IngestController extends BaseController {
    private final IngestTaskService ingestTaskService;

    @Operation(summary = "接入任务列表")
    @PreAuthorize("hasAuthority('ingest:task:list')")
    @GetMapping("/tasks")
    public TableDataInfo<IngestTask> listTasks(IngestTask query, PageDomain page) {
        startPage(page);
        return getDataTable(ingestTaskService.listTasks(query));
    }

    @Operation(summary = "接入任务详情")
    @GetMapping("/tasks/{id}")
    public AjaxResult<IngestTask> getTask(@PathVariable Long id) {
        return AjaxResult.success(ingestTaskService.getById(id));
    }

    @Operation(summary = "新增接入任务")
    @PreAuthorize("hasAuthority('ingest:task:add')")
    @PostMapping("/tasks")
    public AjaxResult<Void> addTask(@RequestBody IngestTask task, @AuthenticationPrincipal LoginUser user) {
        ingestTaskService.addTask(task, user.getUserId());
        return AjaxResult.success();
    }

    @Operation(summary = "修改接入任务")
    @PreAuthorize("hasAuthority('ingest:task:edit')")
    @PutMapping("/tasks/{id}")
    public AjaxResult<Void> editTask(@PathVariable Long id, @RequestBody IngestTask task,
                                     @AuthenticationPrincipal LoginUser user) {
        task.setId(id);
        ingestTaskService.editTask(task, user.getUserId());
        return AjaxResult.success();
    }

    @Operation(summary = "修改任务状态")
    @PreAuthorize("hasAuthority('ingest:task:edit')")
    @PatchMapping("/tasks/{id}/status")
    public AjaxResult<Void> updateStatus(@PathVariable Long id, @RequestBody Map<String, Integer> body) {
        ingestTaskService.updateStatus(id, body.get("status"));
        return AjaxResult.success();
    }

    @Operation(summary = "手动触发执行")
    @PreAuthorize("hasAuthority('ingest:task:edit')")
    @PostMapping("/tasks/{id}/trigger")
    public AjaxResult<IngestRecord> trigger(@PathVariable Long id, @AuthenticationPrincipal LoginUser user) {
        return AjaxResult.success(ingestTaskService.triggerManually(id, user.getUserId()));
    }

    @Operation(summary = "删除接入任务")
    @PreAuthorize("hasAuthority('ingest:task:delete')")
    @DeleteMapping("/tasks/{id}")
    public AjaxResult<Void> removeTask(@PathVariable Long id) {
        ingestTaskService.removeTask(id);
        return AjaxResult.success();
    }

    @Operation(summary = "接入记录列表")
    @PreAuthorize("hasAuthority('ingest:record:list')")
    @GetMapping("/records")
    public TableDataInfo<IngestRecord> listRecords(@RequestParam(required = false) Long taskId,
                                                   @RequestParam(required = false) String status,
                                                   PageDomain page) {
        startPage(page);
        return getDataTable(ingestTaskService.listRecords(taskId, status));
    }
}
