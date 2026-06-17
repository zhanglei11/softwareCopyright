package com.imaging.scheduler.admin.controller.task;

import com.imaging.scheduler.common.core.AjaxResult;
import com.imaging.scheduler.common.core.TableDataInfo;
import com.imaging.scheduler.framework.web.BaseController;
import com.imaging.scheduler.system.domain.task.TaskInfo;
import com.imaging.scheduler.system.dto.req.TaskAddReq;
import com.imaging.scheduler.system.dto.req.TaskAssignReq;
import com.imaging.scheduler.system.dto.req.TaskQueryReq;
import com.imaging.scheduler.system.service.task.TaskInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "任务管理", description = "采集任务CRUD及调度操作")
@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskInfoController extends BaseController {

    private final TaskInfoService taskInfoService;

    @Operation(summary = "任务分页列表")
    @PreAuthorize("hasAuthority('task:info:list')")
    @GetMapping
    public TableDataInfo<TaskInfo> list(TaskQueryReq req) {
        return taskInfoService.getTaskList(req);
    }

    @Operation(summary = "任务详情")
    @PreAuthorize("hasAuthority('task:info:list')")
    @GetMapping("/{id}")
    public AjaxResult<TaskInfo> detail(@PathVariable("id") Long id) {
        return AjaxResult.success(taskInfoService.getTaskById(id));
    }

    @Operation(summary = "创建任务")
    @PreAuthorize("hasAuthority('task:info:add')")
    @PostMapping
    public AjaxResult<Void> add(@Valid @RequestBody TaskAddReq req) {
        taskInfoService.addTask(req, getUserId());
        return AjaxResult.success();
    }

    @Operation(summary = "编辑任务")
    @PreAuthorize("hasAuthority('task:info:edit')")
    @PutMapping("/{id}")
    public AjaxResult<Void> edit(@PathVariable("id") Long id, @Valid @RequestBody TaskAddReq req) {
        taskInfoService.editTask(id, req, getUserId());
        return AjaxResult.success();
    }

    @Operation(summary = "分配设备")
    @PreAuthorize("hasAuthority('task:info:assign')")
    @PostMapping("/{id}/devices")
    public AjaxResult<Void> assignDevices(@PathVariable("id") Long id, @RequestBody TaskAssignReq req) {
        taskInfoService.assignDevices(id, req, getUserId());
        return AjaxResult.success();
    }

    @Operation(summary = "解绑所有设备")
    @PreAuthorize("hasAuthority('task:info:assign')")
    @DeleteMapping("/{id}/devices")
    public AjaxResult<Void> unassignDevices(@PathVariable("id") Long id) {
        taskInfoService.unassignDevices(id, getUserId());
        return AjaxResult.success();
    }

    @Operation(summary = "启动任务")
    @PreAuthorize("hasAuthority('task:info:start')")
    @PostMapping("/{id}/start")
    public AjaxResult<Void> start(@PathVariable("id") Long id) {
        taskInfoService.startTask(id, getUserId());
        return AjaxResult.success();
    }

    @Operation(summary = "完成任务")
    @PreAuthorize("hasAuthority('task:info:complete')")
    @PostMapping("/{id}/complete")
    public AjaxResult<Void> complete(@PathVariable("id") Long id) {
        taskInfoService.completeTask(id, getUserId());
        return AjaxResult.success();
    }

    @Operation(summary = "取消任务")
    @PreAuthorize("hasAuthority('task:info:cancel')")
    @PostMapping("/{id}/cancel")
    public AjaxResult<Void> cancel(@PathVariable("id") Long id) {
        taskInfoService.cancelTask(id, getUserId());
        return AjaxResult.success();
    }

    @Operation(summary = "删除任务")
    @PreAuthorize("hasAuthority('task:info:delete')")
    @DeleteMapping("/{id}")
    public AjaxResult<Void> delete(@PathVariable("id") Long id) {
        taskInfoService.deleteTask(id, getUserId());
        return AjaxResult.success();
    }
}
