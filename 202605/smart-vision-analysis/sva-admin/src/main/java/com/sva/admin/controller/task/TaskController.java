package com.sva.admin.controller.task;

import com.sva.common.core.controller.BaseController;
import com.sva.common.core.domain.AjaxResult;
import com.sva.common.core.domain.TableDataInfo;
import com.sva.framework.security.LoginUser;
import com.sva.system.domain.RecognitionTask;
import com.sva.system.query.TaskQuery;
import com.sva.system.service.IRecognitionTaskService;
import com.sva.system.vo.TaskProgressVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "识别任务管理")
@RestController
@RequestMapping("/api/task")
@RequiredArgsConstructor
public class TaskController extends BaseController {

    private final IRecognitionTaskService taskService;

    @Operation(summary = "任务列表")
    @PreAuthorize("hasAuthority('task:list')")
    @GetMapping
    public TableDataInfo list(TaskQuery query,
                              @RequestParam(defaultValue = "1") int pageNum,
                              @RequestParam(defaultValue = "10") int pageSize) {
        startPage(pageNum, pageSize);
        return getDataTable(taskService.list(query));
    }

    @Operation(summary = "任务详情")
    @GetMapping("/{id}")
    public AjaxResult<RecognitionTask> getInfo(@PathVariable Long id) {
        return AjaxResult.success(taskService.getById(id));
    }

    @Operation(summary = "创建任务")
    @PreAuthorize("hasAuthority('task:create')")
    @PostMapping
    public AjaxResult<RecognitionTask> create(@RequestBody RecognitionTask task,
                                              @AuthenticationPrincipal LoginUser user) {
        return AjaxResult.success(taskService.create(task, user.getUserId()));
    }

    @Operation(summary = "启动任务")
    @PreAuthorize("hasAuthority('task:create')")
    @PostMapping("/{id}/start")
    public AjaxResult<Void> start(@PathVariable Long id) {
        taskService.start(id);
        return AjaxResult.success();
    }

    @Operation(summary = "取消任务")
    @PreAuthorize("hasAuthority('task:create')")
    @PostMapping("/{id}/cancel")
    public AjaxResult<Void> cancel(@PathVariable Long id) {
        taskService.cancel(id);
        return AjaxResult.success();
    }

    @Operation(summary = "任务进度")
    @GetMapping("/{id}/progress")
    public AjaxResult<TaskProgressVO> progress(@PathVariable Long id) {
        return AjaxResult.success(taskService.getProgress(id));
    }

    @Operation(summary = "删除任务")
    @PreAuthorize("hasAuthority('task:delete')")
    @DeleteMapping("/{id}")
    public AjaxResult<Void> delete(@PathVariable Long id) {
        taskService.deleteById(id);
        return AjaxResult.success();
    }
}
