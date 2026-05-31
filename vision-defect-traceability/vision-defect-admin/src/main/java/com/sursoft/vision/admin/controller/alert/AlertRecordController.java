package com.sursoft.vision.admin.controller.alert;

import com.sursoft.vision.common.core.AjaxResult;
import com.sursoft.vision.common.core.TableDataInfo;
import com.sursoft.vision.framework.security.LoginUser;
import com.sursoft.vision.system.domain.AlertRecord;
import com.sursoft.vision.system.dto.AlertHandleDTO;
import com.sursoft.vision.system.query.AlertRecordQuery;
import com.sursoft.vision.system.service.AlertRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/alerts/records")
@Tag(name = "告警记录管理")
@RequiredArgsConstructor
public class AlertRecordController {

    private final AlertRecordService alertRecordService;

    @GetMapping
    @Operation(summary = "查询告警记录列表")
    @PreAuthorize("hasAuthority('alert:record:list')")
    public TableDataInfo<AlertRecord> list(AlertRecordQuery query) {
        return alertRecordService.list(query);
    }

    @PatchMapping("/{id}/handle")
    @Operation(summary = "处理告警记录")
    @PreAuthorize("hasAuthority('alert:record:handle')")
    public AjaxResult<Void> handle(@PathVariable Long id,
            @Valid @RequestBody AlertHandleDTO dto,
            @AuthenticationPrincipal LoginUser loginUser) {
        alertRecordService.handle(id, dto, loginUser.getUser().getId());
        return AjaxResult.success();
    }
}
