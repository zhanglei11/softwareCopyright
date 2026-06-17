package com.sva.admin.controller.system;

import com.sva.common.core.controller.BaseController;
import com.sva.common.core.domain.AjaxResult;
import com.sva.common.core.domain.TableDataInfo;
import com.sva.system.query.LogQuery;
import com.sva.system.service.ISysOperationLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "操作日志")
@RestController
@RequestMapping("/api/system/logs")
@RequiredArgsConstructor
public class SysOperationLogController extends BaseController {

    private final ISysOperationLogService logService;

    @Operation(summary = "操作日志列表")
    @PreAuthorize("hasAuthority('system:log:list')")
    @GetMapping
    public TableDataInfo list(LogQuery query,
                              @RequestParam(defaultValue = "1") int pageNum,
                              @RequestParam(defaultValue = "10") int pageSize) {
        startPage(pageNum, pageSize);
        return getDataTable(logService.list(query));
    }

    @Operation(summary = "清空日志")
    @PreAuthorize("hasAuthority('system:log:delete')")
    @DeleteMapping
    public AjaxResult<Void> clear() {
        logService.clear();
        return AjaxResult.success();
    }
}
