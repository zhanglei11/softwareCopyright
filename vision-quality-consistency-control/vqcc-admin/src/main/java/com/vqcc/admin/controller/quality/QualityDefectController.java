package com.vqcc.admin.controller.quality;

import com.vqcc.common.result.AjaxResult;
import com.vqcc.common.result.TableDataInfo;
import com.vqcc.framework.security.LoginUser;
import com.vqcc.system.domain.QualityDefect;
import com.vqcc.system.domain.QualityDefectDispose;
import com.vqcc.system.dto.request.DefectDisposeReq;
import com.vqcc.system.dto.request.DefectIgnoreReq;
import com.vqcc.system.dto.request.DefectVerifyReq;
import com.vqcc.system.service.IQualityDefectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "不合格品管理")
@RestController
@RequestMapping("/api/v1/quality/defects")
@RequiredArgsConstructor
public class QualityDefectController {

    private final IQualityDefectService defectService;

    @Operation(summary = "不合格品列表")
    @PreAuthorize("hasAuthority('quality:defect:list')")
    @GetMapping
    public AjaxResult<TableDataInfo<QualityDefect>> list(
            @RequestParam(required = false) Long taskId,
            @RequestParam(required = false) Integer disposeStatus,
            @RequestParam(required = false) Integer verifyStatus,
            @RequestParam(required = false) String imageId) {
        return AjaxResult.ok(TableDataInfo.ok(defectService.list(taskId, disposeStatus, verifyStatus, imageId)));
    }

    @Operation(summary = "不合格品详情")
    @PreAuthorize("hasAuthority('quality:defect:list')")
    @GetMapping("/{id}")
    public AjaxResult<QualityDefect> getById(@PathVariable Long id) {
        return AjaxResult.ok(defectService.getById(id));
    }

    @Operation(summary = "处置不合格品")
    @PreAuthorize("hasAuthority('quality:defect:dispose')")
    @PostMapping("/dispose")
    public AjaxResult<Void> dispose(@RequestBody DefectDisposeReq req,
                                     @AuthenticationPrincipal LoginUser loginUser) {
        defectService.dispose(req, loginUser.getUserId());
        return AjaxResult.ok(null);
    }

    @Operation(summary = "忽略不合格品")
    @PreAuthorize("hasAuthority('quality:defect:dispose')")
    @PostMapping("/ignore")
    public AjaxResult<Void> ignore(@RequestBody DefectIgnoreReq req,
                                    @AuthenticationPrincipal LoginUser loginUser) {
        defectService.ignore(req, loginUser.getUserId());
        return AjaxResult.ok(null);
    }

    @Operation(summary = "验证处置结果")
    @PreAuthorize("hasAuthority('quality:defect:verify')")
    @PostMapping("/verify")
    public AjaxResult<Void> verify(@RequestBody DefectVerifyReq req,
                                    @AuthenticationPrincipal LoginUser loginUser) {
        defectService.verify(req, loginUser.getUserId());
        return AjaxResult.ok(null);
    }

    @Operation(summary = "处置历史记录")
    @PreAuthorize("hasAuthority('quality:defect:list')")
    @GetMapping("/{id}/history")
    public AjaxResult<List<QualityDefectDispose>> getHistory(@PathVariable Long id) {
        return AjaxResult.ok(defectService.getDisposeHistory(id));
    }
}
