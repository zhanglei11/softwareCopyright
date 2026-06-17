package com.sursoft.iidp.system.storage.controller;

import com.sursoft.iidp.common.core.AjaxResult;
import com.sursoft.iidp.common.core.PageDomain;
import com.sursoft.iidp.common.core.TableDataInfo;
import com.sursoft.iidp.framework.security.LoginUser;
import com.sursoft.iidp.framework.web.BaseController;
import com.sursoft.iidp.system.storage.domain.StorageCleanLog;
import com.sursoft.iidp.system.storage.domain.StorageCleanRule;
import com.sursoft.iidp.system.storage.domain.StorageOverview;
import com.sursoft.iidp.system.storage.service.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@Tag(name = "数据存储管理")
@RestController
@RequestMapping("/api/storage")
@RequiredArgsConstructor
public class StorageController extends BaseController {
    private final StorageService storageService;

    @Operation(summary = "存储空间总览")
    @PreAuthorize("hasAuthority('storage:overview:view')")
    @GetMapping("/overview")
    public AjaxResult<StorageOverview> overview() {
        return AjaxResult.success(storageService.getOverview());
    }

    @Operation(summary = "清理规则列表")
    @PreAuthorize("hasAuthority('storage:clean:list')")
    @GetMapping("/clean-rules")
    public TableDataInfo<StorageCleanRule> listRules(StorageCleanRule query, PageDomain page) {
        startPage(page);
        return getDataTable(storageService.listRules(query));
    }

    @Operation(summary = "新增清理规则")
    @PreAuthorize("hasAuthority('storage:clean:add')")
    @PostMapping("/clean-rules")
    public AjaxResult<Void> addRule(@RequestBody StorageCleanRule rule, @AuthenticationPrincipal LoginUser user) {
        storageService.addRule(rule, user.getUserId());
        return AjaxResult.success();
    }

    @Operation(summary = "修改清理规则")
    @PreAuthorize("hasAuthority('storage:clean:edit')")
    @PutMapping("/clean-rules/{id}")
    public AjaxResult<Void> editRule(@PathVariable Long id, @RequestBody StorageCleanRule rule,
                                     @AuthenticationPrincipal LoginUser user) {
        rule.setId(id);
        storageService.editRule(rule, user.getUserId());
        return AjaxResult.success();
    }

    @Operation(summary = "修改规则状态")
    @PreAuthorize("hasAuthority('storage:clean:edit')")
    @PatchMapping("/clean-rules/{id}/status")
    public AjaxResult<Void> updateStatus(@PathVariable Long id, @RequestBody Map<String, Integer> body) {
        storageService.updateRuleStatus(id, body.get("status"));
        return AjaxResult.success();
    }

    @Operation(summary = "删除清理规则")
    @PreAuthorize("hasAuthority('storage:clean:delete')")
    @DeleteMapping("/clean-rules/{id}")
    public AjaxResult<Void> removeRule(@PathVariable Long id) {
        storageService.removeRule(id);
        return AjaxResult.success();
    }

    @Operation(summary = "手动执行清理")
    @PreAuthorize("hasAuthority('storage:clean:execute')")
    @PostMapping("/clean-rules/{id}/execute")
    public AjaxResult<StorageCleanLog> executeClean(@PathVariable Long id, @AuthenticationPrincipal LoginUser user) {
        return AjaxResult.success(storageService.executeClean(id, user.getUserId()));
    }

    @Operation(summary = "清理日志列表")
    @PreAuthorize("hasAuthority('storage:clean:log')")
    @GetMapping("/clean-logs")
    public TableDataInfo<StorageCleanLog> listLogs(@RequestParam(required = false) Long ruleId, PageDomain page) {
        startPage(page);
        return getDataTable(storageService.listCleanLogs(ruleId));
    }
}
