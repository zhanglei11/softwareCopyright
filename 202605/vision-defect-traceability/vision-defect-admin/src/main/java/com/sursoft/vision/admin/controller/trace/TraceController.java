package com.sursoft.vision.admin.controller.trace;

import com.sursoft.vision.common.core.AjaxResult;
import com.sursoft.vision.system.service.TraceService;
import com.sursoft.vision.system.vo.BatchTraceVO;
import com.sursoft.vision.system.vo.ProductTraceVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/trace")
@Tag(name = "追溯管理")
@RequiredArgsConstructor
public class TraceController {

    private final TraceService traceService;

    @GetMapping("/batch")
    @Operation(summary = "批次追溯", description = "按批次号查询质量全貌")
    @PreAuthorize("hasAuthority('trace:query')")
    public AjaxResult<BatchTraceVO> traceBatch(
            @RequestParam("batchNo") String batchNo,
            @RequestParam(value = "lineId", required = false) Long lineId) {
        return AjaxResult.success(traceService.traceBatch(batchNo, lineId));
    }

    @GetMapping("/product")
    @Operation(summary = "产品追溯", description = "按序列号查询单品检测历史")
    @PreAuthorize("hasAuthority('trace:query')")
    public AjaxResult<ProductTraceVO> traceProduct(@RequestParam("serialNo") String serialNo) {
        return AjaxResult.success(traceService.traceProduct(serialNo));
    }
}
