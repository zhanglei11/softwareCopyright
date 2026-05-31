package com.angu.matcher.admin.controller;

import com.angu.matcher.common.result.AjaxResult;
import com.angu.matcher.framework.web.BaseController;
import com.angu.matcher.system.dto.MatchConfigRequest;
import com.angu.matcher.system.service.IMatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "智能匹配")
@RestController
@RequestMapping("/api/match")
@RequiredArgsConstructor
public class MatchController extends BaseController {

    private final IMatchService matchService;

    @Operation(summary = "发起匹配")
    @PreAuthorize("hasAuthority('match:match:execute')")
    @PostMapping("/run")
    public AjaxResult<?> runMatch(@RequestBody Map<String, Long> body) {
        return AjaxResult.success(matchService.runMatch(body.get("positionId")));
    }

    @Operation(summary = "查看某职位最近匹配结果")
    @PreAuthorize("hasAuthority('match:match:execute')")
    @GetMapping("/results/{positionId}")
    public AjaxResult<?> getResults(@PathVariable Long positionId) {
        return AjaxResult.success(matchService.getResults(positionId));
    }

    @Operation(summary = "获取匹配规则配置")
    @PreAuthorize("hasAuthority('match:config:view')")
    @GetMapping("/config")
    public AjaxResult<?> getConfig() {
        return AjaxResult.success(matchService.getConfig());
    }

    @Operation(summary = "保存匹配规则配置")
    @PreAuthorize("hasAuthority('match:config:edit')")
    @PutMapping("/config")
    public AjaxResult<Void> updateConfig(@Valid @RequestBody MatchConfigRequest req) {
        matchService.updateConfig(req, getUserId());
        return AjaxResult.success();
    }
}
