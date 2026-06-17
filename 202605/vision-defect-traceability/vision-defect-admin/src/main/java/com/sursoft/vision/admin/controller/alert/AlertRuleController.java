package com.sursoft.vision.admin.controller.alert;

import com.sursoft.vision.common.core.AjaxResult;
import com.sursoft.vision.common.core.TableDataInfo;
import com.sursoft.vision.framework.security.LoginUser;
import com.sursoft.vision.system.domain.AlertRule;
import com.sursoft.vision.system.dto.AlertRuleDTO;
import com.sursoft.vision.system.service.AlertRuleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/alerts/rules")
@Tag(name = "告警规则管理")
@RequiredArgsConstructor
public class AlertRuleController {

    private final AlertRuleService ruleService;

    @GetMapping
    @Operation(summary = "查询告警规则列表")
    @PreAuthorize("hasAuthority('alert:rule:list')")
    public TableDataInfo<AlertRule> list(@RequestParam(value = "status", required=false) Integer status,
            @RequestParam(value = "pageNum", defaultValue="1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue="10") int pageSize) {
        return ruleService.list(status, pageNum, pageSize);
    }

    @PostMapping
    @Operation(summary = "新增告警规则")
    @PreAuthorize("hasAuthority('alert:rule:add')")
    public AjaxResult<Void> add(@Valid @RequestBody AlertRuleDTO dto,
            @AuthenticationPrincipal LoginUser loginUser) {
        ruleService.add(dto, loginUser.getUser().getId());
        return AjaxResult.success();
    }

    @PutMapping("/{id}")
    @Operation(summary = "编辑告警规则")
    @PreAuthorize("hasAuthority('alert:rule:edit')")
    public AjaxResult<Void> edit(@PathVariable Long id, @Valid @RequestBody AlertRuleDTO dto) {
        ruleService.edit(id, dto);
        return AjaxResult.success();
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "启用/停用规则")
    @PreAuthorize("hasAuthority('alert:rule:edit')")
    public AjaxResult<Void> updateStatus(@PathVariable Long id, @RequestBody Map<String, Integer> body) {
        ruleService.updateStatus(id, body.get("status"));
        return AjaxResult.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除告警规则")
    @PreAuthorize("hasAuthority('alert:rule:delete')")
    public AjaxResult<Void> delete(@PathVariable Long id) {
        ruleService.delete(id);
        return AjaxResult.success();
    }
}
