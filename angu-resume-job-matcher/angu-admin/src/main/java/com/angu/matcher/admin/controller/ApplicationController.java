package com.angu.matcher.admin.controller;

import com.angu.matcher.common.result.AjaxResult;
import com.angu.matcher.common.result.TableDataInfo;
import com.angu.matcher.framework.web.BaseController;
import com.angu.matcher.system.dto.ApplicationCreateRequest;
import com.angu.matcher.system.dto.ApplicationStatusRequest;
import com.angu.matcher.system.service.IJobApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "投递记录")
@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController extends BaseController {

    private final IJobApplicationService applicationService;

    @Operation(summary = "投递分页列表")
    @PreAuthorize("hasAuthority('application:list')")
    @GetMapping
    public AjaxResult<TableDataInfo<?>> list(
            @RequestParam(required = false) Long positionId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        startPage(page, size);
        return AjaxResult.success(getDataTable(applicationService.listApplications(positionId, status)));
    }

    @Operation(summary = "投递详情")
    @PreAuthorize("hasAuthority('application:list')")
    @GetMapping("/{id}")
    public AjaxResult<?> getById(@PathVariable Long id) {
        return AjaxResult.success(applicationService.getById(id));
    }

    @Operation(summary = "创建投递记录")
    @PreAuthorize("hasAuthority('application:add')")
    @PostMapping
    public AjaxResult<?> create(@Valid @RequestBody ApplicationCreateRequest req) {
        return AjaxResult.success(applicationService.createApplication(req, getUserId()));
    }

    @Operation(summary = "变更投递状态")
    @PreAuthorize("hasAuthority('application:edit')")
    @PutMapping("/{id}/status")
    public AjaxResult<Void> changeStatus(@PathVariable Long id,
                                          @Valid @RequestBody ApplicationStatusRequest req) {
        applicationService.changeStatus(id, req, getUserId(), getRealName());
        return AjaxResult.success();
    }

    @Operation(summary = "操作日志")
    @PreAuthorize("hasAuthority('application:list')")
    @GetMapping("/{id}/logs")
    public AjaxResult<?> getLogs(@PathVariable Long id) {
        return AjaxResult.success(applicationService.getLogs(id));
    }
}
