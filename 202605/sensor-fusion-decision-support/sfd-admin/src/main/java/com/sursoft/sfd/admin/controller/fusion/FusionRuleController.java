package com.sursoft.sfd.admin.controller.fusion;

import com.sursoft.sfd.common.core.AjaxResult;
import com.sursoft.sfd.framework.web.BaseController;
import com.sursoft.sfd.system.domain.FusionRule;
import com.sursoft.sfd.system.domain.FusionWeight;
import com.sursoft.sfd.system.service.IFusionSchemeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "融合规则管理")
@RestController
@RequestMapping("/api/fusion/schemes/{schemeId}")
@RequiredArgsConstructor
public class FusionRuleController extends BaseController {
    private final IFusionSchemeService schemeService;

    @Operation(summary = "获取方案规则列表")
    @GetMapping("/rules")
    public AjaxResult<List<FusionRule>> getRules(@PathVariable Long schemeId) { return AjaxResult.ok(schemeService.getRules(schemeId)); }

    @Operation(summary = "新增规则")
    @PostMapping("/rules")
    public AjaxResult<Void> addRule(@PathVariable Long schemeId, @RequestBody FusionRule rule) { rule.setSchemeId(schemeId); schemeService.addRule(rule, getCurrentUserId()); return AjaxResult.ok(); }

    @Operation(summary = "编辑规则")
    @PutMapping("/rules/{ruleId}")
    public AjaxResult<Void> editRule(@PathVariable Long schemeId, @PathVariable Long ruleId, @RequestBody FusionRule rule) { schemeService.editRule(ruleId, rule, getCurrentUserId()); return AjaxResult.ok(); }

    @Operation(summary = "删除规则")
    @DeleteMapping("/rules/{ruleId}")
    public AjaxResult<Void> deleteRule(@PathVariable Long schemeId, @PathVariable Long ruleId) { schemeService.deleteRule(ruleId); return AjaxResult.ok(); }

    @Operation(summary = "获取方案权重配置")
    @GetMapping("/weights")
    public AjaxResult<List<FusionWeight>> getWeights(@PathVariable Long schemeId) { return AjaxResult.ok(schemeService.getWeights(schemeId)); }

    @Operation(summary = "批量保存权重配置")
    @PutMapping("/weights")
    public AjaxResult<Void> saveWeights(@PathVariable Long schemeId, @RequestBody List<FusionWeight> weights) { schemeService.saveWeights(schemeId, weights, getCurrentUserId()); return AjaxResult.ok(); }
}
