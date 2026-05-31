package com.sursoft.sfd.admin.controller.fusion;

import com.github.pagehelper.PageInfo;
import com.sursoft.sfd.common.core.AjaxResult;
import com.sursoft.sfd.common.core.PageResult;
import com.sursoft.sfd.framework.web.BaseController;
import com.sursoft.sfd.system.domain.FusionResult;
import com.sursoft.sfd.system.service.IFusionResultService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "融合结果查询")
@RestController
@RequestMapping("/api/fusion/results")
@RequiredArgsConstructor
public class FusionResultController extends BaseController {
    private final IFusionResultService resultService;

    @Operation(summary = "融合结果列表")
    @GetMapping
    public AjaxResult<PageResult<FusionResult>> list(
            @RequestParam(required = false) Long schemeId,
            @RequestParam(required = false) Integer resultStatus,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return getDataTable(pageNum, pageSize, () -> resultService.list(schemeId, resultStatus, startTime, endTime));
    }

    @Operation(summary = "融合结果详情")
    @GetMapping("/{id}")
    public AjaxResult<FusionResult> getById(@PathVariable Long id) { return AjaxResult.ok(resultService.getById(id)); }
}
