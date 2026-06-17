package com.sursoft.sfd.admin.controller.fusion;

import com.sursoft.sfd.common.core.AjaxResult;
import com.sursoft.sfd.framework.web.BaseController;
import com.sursoft.sfd.system.domain.FusionScheme;
import com.sursoft.sfd.system.service.IFusionSchemeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "融合方案管理")
@RestController
@RequestMapping("/api/fusion/schemes")
@RequiredArgsConstructor
public class FusionSchemeController extends BaseController {
    private final IFusionSchemeService schemeService;

    @Operation(summary = "方案列表")
    @GetMapping
    public AjaxResult<List<FusionScheme>> list(@RequestParam(required = false) String keyword, @RequestParam(required = false) Integer status) { return AjaxResult.ok(schemeService.list(keyword, status)); }

    @Operation(summary = "方案详情")
    @GetMapping("/{id}")
    public AjaxResult<FusionScheme> getById(@PathVariable Long id) { return AjaxResult.ok(schemeService.getById(id)); }

    @Operation(summary = "新增方案")
    @PostMapping
    public AjaxResult<Void> add(@RequestBody FusionScheme scheme) { schemeService.add(scheme, getCurrentUserId()); return AjaxResult.ok(); }

    @Operation(summary = "编辑方案")
    @PutMapping("/{id}")
    public AjaxResult<Void> edit(@PathVariable Long id, @RequestBody FusionScheme scheme) { schemeService.edit(id, scheme, getCurrentUserId()); return AjaxResult.ok(); }

    @Operation(summary = "删除方案")
    @DeleteMapping("/{id}")
    public AjaxResult<Void> delete(@PathVariable Long id) { schemeService.delete(id); return AjaxResult.ok(); }

    @Operation(summary = "启用/禁用方案")
    @PutMapping("/{id}/status")
    public AjaxResult<Void> status(@PathVariable Long id, @RequestParam Integer status) { schemeService.updateStatus(id, status, getCurrentUserId()); return AjaxResult.ok(); }
}
