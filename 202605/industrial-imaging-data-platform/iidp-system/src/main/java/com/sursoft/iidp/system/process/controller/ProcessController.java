package com.sursoft.iidp.system.process.controller;

import com.sursoft.iidp.common.core.AjaxResult;
import com.sursoft.iidp.common.core.PageDomain;
import com.sursoft.iidp.common.core.TableDataInfo;
import com.sursoft.iidp.framework.security.LoginUser;
import com.sursoft.iidp.framework.web.BaseController;
import com.sursoft.iidp.system.process.domain.ProcessExecution;
import com.sursoft.iidp.system.process.domain.ProcessTask;
import com.sursoft.iidp.system.process.service.ProcessTaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@Tag(name = "数据处理管理")
@RestController
@RequestMapping("/api/process")
@RequiredArgsConstructor
public class ProcessController extends BaseController {
    private final ProcessTaskService processTaskService;

    @Operation(summary = "处理任务列表")
    @PreAuthorize("hasAuthority('process:task:list')")
    @GetMapping("/tasks")
    public TableDataInfo<ProcessTask> listTasks(ProcessTask query, PageDomain page) {
        startPage(page);
        return getDataTable(processTaskService.listTasks(query));
    }

    @Operation(summary = "处理任务详情")
    @GetMapping("/tasks/{id}")
    public AjaxResult<ProcessTask> getTask(@PathVariable Long id) {
        return AjaxResult.success(processTaskService.getById(id));
    }

    @Operation(summary = "新增处理任务")
    @PreAuthorize("hasAuthority('process:task:add')")
    @PostMapping("/tasks")
    public AjaxResult<Void> addTask(@RequestBody ProcessTask task, @AuthenticationPrincipal LoginUser user) {
        processTaskService.addTask(task, user.getUserId());
        return AjaxResult.success();
    }

    @Operation(summary = "修改处理任务")
    @PreAuthorize("hasAuthority('process:task:edit')")
    @PutMapping("/tasks/{id}")
    public AjaxResult<Void> editTask(@PathVariable Long id, @RequestBody ProcessTask task,
                                     @AuthenticationPrincipal LoginUser user) {
        task.setId(id);
        processTaskService.editTask(task, user.getUserId());
        return AjaxResult.success();
    }

    @Operation(summary = "修改任务状态")
    @PreAuthorize("hasAuthority('process:task:edit')")
    @PatchMapping("/tasks/{id}/status")
    public AjaxResult<Void> updateStatus(@PathVariable Long id, @RequestBody Map<String, Integer> body) {
        processTaskService.updateStatus(id, body.get("status"));
        return AjaxResult.success();
    }

    @Operation(summary = "触发执行")
    @PreAuthorize("hasAuthority('process:task:edit')")
    @PostMapping("/tasks/{id}/trigger")
    public AjaxResult<ProcessExecution> trigger(@PathVariable Long id, @AuthenticationPrincipal LoginUser user) {
        return AjaxResult.success(processTaskService.triggerExecution(id, user.getUserId()));
    }

    @Operation(summary = "手动终止")
    @PreAuthorize("hasAuthority('process:task:edit')")
    @PostMapping("/tasks/{id}/terminate")
    public AjaxResult<ProcessExecution> terminate(@PathVariable Long id) {
        return AjaxResult.success(processTaskService.terminateExecution(id));
    }

    @Operation(summary = "执行监控-当前进度")
    @PreAuthorize("hasAuthority('process:execution:view')")
    @GetMapping("/tasks/{id}/running")
    public AjaxResult<ProcessExecution> runningExecution(@PathVariable Long id) {
        return AjaxResult.success(processTaskService.getRunningExecution(id));
    }

    @Operation(summary = "删除处理任务")
    @PreAuthorize("hasAuthority('process:task:delete')")
    @DeleteMapping("/tasks/{id}")
    public AjaxResult<Void> removeTask(@PathVariable Long id) {
        processTaskService.removeTask(id);
        return AjaxResult.success();
    }

    @Operation(summary = "处理结果列表")
    @PreAuthorize("hasAuthority('process:result:list')")
    @GetMapping("/executions")
    public TableDataInfo<ProcessExecution> listExecutions(@RequestParam(required = false) Long taskId,
                                                          @RequestParam(required = false) String status,
                                                          PageDomain page) {
        startPage(page);
        return getDataTable(processTaskService.listExecutions(taskId, status));
    }
}
