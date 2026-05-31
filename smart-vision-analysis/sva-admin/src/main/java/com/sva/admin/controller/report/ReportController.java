package com.sva.admin.controller.report;

import com.sva.common.core.domain.AjaxResult;
import com.sva.system.domain.ReportTaskSummary;
import com.sva.system.service.IReportService;
import com.sva.system.vo.SummaryReportVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "报告管理")
@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
public class ReportController {

    private final IReportService reportService;

    @Operation(summary = "获取任务分析报告")
    @PreAuthorize("hasAuthority('report:view')")
    @GetMapping("/task/{taskId}")
    public AjaxResult<ReportTaskSummary> getTaskReport(@PathVariable("taskId") Long taskId) {
        return AjaxResult.success(reportService.getTaskReport(taskId));
    }

    @Operation(summary = "获取综合汇总报告")
    @PreAuthorize("hasAuthority('report:view')")
    @GetMapping("/summary")
    public AjaxResult<SummaryReportVO> getSummaryReport(
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate) {
        return AjaxResult.success(reportService.getSummaryReport(startDate, endDate));
    }
}
