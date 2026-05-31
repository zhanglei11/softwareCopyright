package com.angu.matcher.admin.controller;

import com.angu.matcher.common.result.AjaxResult;
import com.angu.matcher.common.result.TableDataInfo;
import com.angu.matcher.framework.web.BaseController;
import com.angu.matcher.system.dto.JobPositionRequest;
import com.angu.matcher.system.service.IJobPositionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "职位管理")
@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobPositionController extends BaseController {

    private final IJobPositionService positionService;

    @Operation(summary = "职位分页列表")
    @PreAuthorize("hasAuthority('job:job:list')")
    @GetMapping
    public AjaxResult<TableDataInfo<?>> list(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String jobType,
            @RequestParam(required = false) String eduRequire,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        startPage(page, size);
        return AjaxResult.success(getDataTable(positionService.listPositions(title, department, status, jobType, eduRequire)));
    }

    @Operation(summary = "职位详情")
    @PreAuthorize("hasAuthority('job:job:list')")
    @GetMapping("/{id}")
    public AjaxResult<?> getById(@PathVariable Long id) {
        return AjaxResult.success(positionService.getById(id));
    }

    @Operation(summary = "新建职位")
    @PreAuthorize("hasAuthority('job:job:add')")
    @PostMapping
    public AjaxResult<Void> create(@Valid @RequestBody JobPositionRequest req) {
        positionService.createPosition(req, getUserId());
        return AjaxResult.success();
    }

    @Operation(summary = "编辑职位")
    @PreAuthorize("hasAuthority('job:job:edit')")
    @PutMapping("/{id}")
    public AjaxResult<Void> update(@PathVariable Long id, @Valid @RequestBody JobPositionRequest req) {
        positionService.updatePosition(id, req);
        return AjaxResult.success();
    }

    @Operation(summary = "删除职位（仅 DRAFT）")
    @PreAuthorize("hasAuthority('job:job:delete')")
    @DeleteMapping("/{id}")
    public AjaxResult<Void> delete(@PathVariable Long id) {
        positionService.deletePosition(id);
        return AjaxResult.success();
    }

    @Operation(summary = "发布职位")
    @PreAuthorize("hasAuthority('job:job:publish')")
    @PutMapping("/{id}/publish")
    public AjaxResult<Void> publish(@PathVariable Long id) {
        positionService.publishPosition(id);
        return AjaxResult.success();
    }

    @Operation(summary = "关闭职位")
    @PreAuthorize("hasAuthority('job:job:close')")
    @PutMapping("/{id}/close")
    public AjaxResult<Void> close(@PathVariable Long id) {
        positionService.closePosition(id);
        return AjaxResult.success();
    }
}
