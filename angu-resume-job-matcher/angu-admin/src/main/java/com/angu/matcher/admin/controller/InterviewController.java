package com.angu.matcher.admin.controller;

import com.angu.matcher.common.result.AjaxResult;
import com.angu.matcher.common.result.TableDataInfo;
import com.angu.matcher.framework.web.BaseController;
import com.angu.matcher.system.dto.InterviewRequest;
import com.angu.matcher.system.dto.InterviewResultRequest;
import com.angu.matcher.system.service.IInterviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "面试管理")
@RestController
@RequestMapping("/api/interviews")
@RequiredArgsConstructor
public class InterviewController extends BaseController {

    private final IInterviewService interviewService;

    @Operation(summary = "面试分页列表")
    @PreAuthorize("hasAuthority('interview:list')")
    @GetMapping
    public AjaxResult<TableDataInfo<?>> list(
            @RequestParam(required = false) String interviewer,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        startPage(page, size);
        return AjaxResult.success(getDataTable(interviewService.listInterviews(interviewer)));
    }

    @Operation(summary = "面试详情")
    @PreAuthorize("hasAuthority('interview:list')")
    @GetMapping("/{id}")
    public AjaxResult<?> getById(@PathVariable Long id) {
        return AjaxResult.success(interviewService.getById(id));
    }

    @Operation(summary = "新增面试安排")
    @PreAuthorize("hasAuthority('interview:add')")
    @PostMapping
    public AjaxResult<?> create(@Valid @RequestBody InterviewRequest req) {
        return AjaxResult.success(interviewService.createInterview(req, getUserId(), getRealName()));
    }

    @Operation(summary = "修改面试")
    @PreAuthorize("hasAuthority('interview:edit')")
    @PutMapping("/{id}")
    public AjaxResult<Void> update(@PathVariable Long id, @RequestBody InterviewRequest req) {
        interviewService.updateInterview(id, req);
        return AjaxResult.success();
    }

    @Operation(summary = "填写面试评价与结果")
    @PreAuthorize("hasAuthority('interview:edit')")
    @PutMapping("/{id}/result")
    public AjaxResult<Void> fillResult(@PathVariable Long id,
                                        @Valid @RequestBody InterviewResultRequest req) {
        interviewService.fillResult(id, req, getUserId(), getRealName());
        return AjaxResult.success();
    }
}
