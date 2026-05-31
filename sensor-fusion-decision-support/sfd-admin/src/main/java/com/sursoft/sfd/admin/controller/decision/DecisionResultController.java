package com.sursoft.sfd.admin.controller.decision;

import com.github.pagehelper.PageInfo;
import com.sursoft.sfd.common.core.AjaxResult;
import com.sursoft.sfd.common.core.PageResult;
import com.sursoft.sfd.framework.web.BaseController;
import com.sursoft.sfd.system.domain.DecisionResult;
import com.sursoft.sfd.system.service.IDecisionResultService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@Tag(name = "决策结果查询")
@RestController
@RequestMapping("/api/decision/results")
@RequiredArgsConstructor
public class DecisionResultController extends BaseController {
    private final IDecisionResultService resultService;

    @Operation(summary = "决策结果列表")
    @GetMapping
    public AjaxResult<PageResult<DecisionResult>> list(
            @RequestParam(required = false) Long ruleId,
            @RequestParam(required = false) Long schemeId,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return getDataTable(pageNum, pageSize, () -> resultService.list(ruleId, schemeId, startTime, endTime, keyword));
    }

    @Operation(summary = "决策结果详情")
    @GetMapping("/{id}")
    public AjaxResult<DecisionResult> getById(@PathVariable Long id) { return AjaxResult.ok(resultService.getById(id)); }

    @Operation(summary = "决策结果溯源详情")
    @GetMapping("/{id}/trace")
    public AjaxResult<DecisionResult> trace(@PathVariable Long id) { return AjaxResult.ok(resultService.getWithTrace(id)); }

    @Operation(summary = "触发频率统计")
    @GetMapping("/frequency")
    public AjaxResult<Map<String, Object>> frequency(@RequestParam(required = false) Long schemeId, @RequestParam(required = false) String startTime, @RequestParam(required = false) String endTime) {
        return AjaxResult.ok(resultService.triggerFrequency(schemeId, startTime, endTime));
    }
}
