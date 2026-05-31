package com.vqcc.admin.controller.quality;

import com.vqcc.common.result.AjaxResult;
import com.vqcc.common.result.TableDataInfo;
import com.vqcc.framework.security.LoginUser;
import com.vqcc.system.domain.QualityTemplate;
import com.vqcc.system.dto.request.TemplateCreateReq;
import com.vqcc.system.service.IQualityTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@Tag(name = "质量标准模板管理")
@RestController
@RequestMapping("/api/v1/quality/templates")
@RequiredArgsConstructor
public class QualityTemplateController {

    private final IQualityTemplateService templateService;

    @Operation(summary = "模板列表")
    @PreAuthorize("hasAuthority('quality:template:list')")
    @GetMapping
    public AjaxResult<TableDataInfo<QualityTemplate>> list(
            @RequestParam(required = false) String templateName,
            @RequestParam(required = false) Integer status) {
        return AjaxResult.ok(TableDataInfo.ok(templateService.list(templateName, status)));
    }

    @Operation(summary = "模板详情（含指标）")
    @PreAuthorize("hasAuthority('quality:template:list')")
    @GetMapping("/{id}")
    public AjaxResult<QualityTemplate> getById(@PathVariable Long id) {
        return AjaxResult.ok(templateService.getById(id));
    }

    @Operation(summary = "创建模板")
    @PreAuthorize("hasAuthority('quality:template:add')")
    @PostMapping
    public AjaxResult<Void> create(@RequestBody TemplateCreateReq req,
                                    @AuthenticationPrincipal LoginUser loginUser) {
        templateService.create(req, loginUser.getUserId());
        return AjaxResult.ok(null);
    }

    @Operation(summary = "更新模板")
    @PreAuthorize("hasAuthority('quality:template:edit')")
    @PutMapping("/{id}")
    public AjaxResult<Void> update(@PathVariable Long id, @RequestBody TemplateCreateReq req,
                                    @AuthenticationPrincipal LoginUser loginUser) {
        templateService.update(id, req, loginUser.getUserId());
        return AjaxResult.ok(null);
    }

    @Operation(summary = "删除模板")
    @PreAuthorize("hasAuthority('quality:template:delete')")
    @DeleteMapping("/{id}")
    public AjaxResult<Void> delete(@PathVariable Long id) {
        templateService.delete(id);
        return AjaxResult.ok(null);
    }

    @Operation(summary = "修改状态")
    @PreAuthorize("hasAuthority('quality:template:edit')")
    @PatchMapping("/{id}/status")
    public AjaxResult<Void> updateStatus(@PathVariable Long id, @RequestBody Map<String, Integer> body,
                                          @AuthenticationPrincipal LoginUser loginUser) {
        templateService.updateStatus(id, body.get("status"), loginUser.getUserId());
        return AjaxResult.ok(null);
    }
}
