package com.sursoft.sfd.admin.controller.decision;

import com.sursoft.sfd.common.core.AjaxResult;
import com.sursoft.sfd.framework.web.BaseController;
import com.sursoft.sfd.system.domain.DecisionRule;
import com.sursoft.sfd.system.domain.DecisionCondition;
import com.sursoft.sfd.system.service.IDecisionRuleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "决策规则管理")
@RestController
@RequestMapping("/api/decision")
@RequiredArgsConstructor
public class DecisionRuleController extends BaseController {
    private final IDecisionRuleService ruleService;

    @Operation(summary = "决策规则列表")
    @GetMapping("/rules")
    public AjaxResult<List<DecisionRule>> listRules(@RequestParam(required = false) Long schemeId, @RequestParam(required = false) Integer status, @RequestParam(required = false) String keyword) { return AjaxResult.ok(ruleService.list(schemeId, status, keyword)); }

    @Operation(summary = "决策规则详情")
    @GetMapping("/rules/{id}")
    public AjaxResult<DecisionRule> getRule(@PathVariable Long id) { return AjaxResult.ok(ruleService.getById(id)); }

    @Operation(summary = "新增决策规则")
    @PostMapping("/rules")
    public AjaxResult<Void> addRule(@RequestBody DecisionRule rule) { ruleService.add(rule, getCurrentUserId()); return AjaxResult.ok(); }

    @Operation(summary = "编辑决策规则")
    @PutMapping("/rules/{id}")
    public AjaxResult<Void> editRule(@PathVariable Long id, @RequestBody DecisionRule rule) { ruleService.edit(id, rule, getCurrentUserId()); return AjaxResult.ok(); }

    @Operation(summary = "删除决策规则")
    @DeleteMapping("/rules/{id}")
    public AjaxResult<Void> deleteRule(@PathVariable Long id) { ruleService.delete(id); return AjaxResult.ok(); }

    @Operation(summary = "启用/禁用决策规则")
    @PutMapping("/rules/{id}/status")
    public AjaxResult<Void> ruleStatus(@PathVariable Long id, @RequestParam Integer status) { ruleService.updateStatus(id, status, getCurrentUserId()); return AjaxResult.ok(); }

    @Operation(summary = "决策条件列表")
    @GetMapping("/conditions")
    public AjaxResult<List<DecisionCondition>> listConditions(@RequestParam(required = false) String keyword) { return AjaxResult.ok(ruleService.listConditions(keyword)); }

    @Operation(summary = "新增决策条件")
    @PostMapping("/conditions")
    public AjaxResult<Void> addCondition(@RequestBody DecisionCondition c) { ruleService.addCondition(c, getCurrentUserId()); return AjaxResult.ok(); }

    @Operation(summary = "编辑决策条件")
    @PutMapping("/conditions/{id}")
    public AjaxResult<Void> editCondition(@PathVariable Long id, @RequestBody DecisionCondition c) { ruleService.editCondition(id, c, getCurrentUserId()); return AjaxResult.ok(); }

    @Operation(summary = "删除决策条件")
    @DeleteMapping("/conditions/{id}")
    public AjaxResult<Void> deleteCondition(@PathVariable Long id) { ruleService.deleteCondition(id); return AjaxResult.ok(); }
}
