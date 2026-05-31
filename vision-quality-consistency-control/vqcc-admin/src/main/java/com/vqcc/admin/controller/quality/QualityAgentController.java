package com.vqcc.admin.controller.quality;

import com.vqcc.common.result.AjaxResult;
import com.vqcc.common.result.TableDataInfo;
import com.vqcc.framework.security.LoginUser;
import com.vqcc.system.domain.QualityAgent;
import com.vqcc.system.domain.QualityAgentTask;
import com.vqcc.system.dto.request.AgentDispatchReq;
import com.vqcc.system.dto.request.AgentRegisterReq;
import com.vqcc.system.dto.request.AgentUpdateReq;
import com.vqcc.system.service.IQualityAgentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "智能体管理")
@RestController
@RequestMapping("/api/v1/agents")
@RequiredArgsConstructor
public class QualityAgentController {

    private final IQualityAgentService agentService;

    @Operation(summary = "智能体列表")
    @PreAuthorize("hasAuthority('agent:list')")
    @GetMapping
    public AjaxResult<TableDataInfo<QualityAgent>> list(
            @RequestParam(required = false) String agentName,
            @RequestParam(required = false) Integer agentType,
            @RequestParam(required = false) Integer status) {
        return AjaxResult.ok(TableDataInfo.ok(agentService.list(agentName, agentType, status)));
    }

    @Operation(summary = "智能体详情")
    @PreAuthorize("hasAuthority('agent:list')")
    @GetMapping("/{id}")
    public AjaxResult<QualityAgent> getById(@PathVariable Long id) {
        return AjaxResult.ok(agentService.getById(id));
    }

    @Operation(summary = "注册智能体")
    @PreAuthorize("hasAuthority('agent:add')")
    @PostMapping
    public AjaxResult<Void> register(@Valid @RequestBody AgentRegisterReq req,
                                      @AuthenticationPrincipal LoginUser loginUser) {
        agentService.register(req, loginUser.getUserId());
        return AjaxResult.ok(null);
    }

    @Operation(summary = "更新智能体")
    @PreAuthorize("hasAuthority('agent:edit')")
    @PutMapping
    public AjaxResult<Void> update(@Valid @RequestBody AgentUpdateReq req,
                                    @AuthenticationPrincipal LoginUser loginUser) {
        agentService.update(req, loginUser.getUserId());
        return AjaxResult.ok(null);
    }

    @Operation(summary = "删除智能体")
    @PreAuthorize("hasAuthority('agent:delete')")
    @DeleteMapping("/{id}")
    public AjaxResult<Void> delete(@PathVariable Long id) {
        agentService.delete(id);
        return AjaxResult.ok(null);
    }

    @Operation(summary = "上线/下线智能体 (status: 0=离线 1=空闲)")
    @PreAuthorize("hasAuthority('agent:edit')")
    @PostMapping("/{id}/toggle-status")
    public AjaxResult<Void> toggleStatus(@PathVariable Long id,
                                          @RequestParam Integer status,
                                          @AuthenticationPrincipal LoginUser loginUser) {
        agentService.toggleStatus(id, status, loginUser.getUserId());
        return AjaxResult.ok(null);
    }

    @Operation(summary = "调度任务给智能体")
    @PreAuthorize("hasAuthority('agent:dispatch')")
    @PostMapping("/dispatch")
    public AjaxResult<QualityAgentTask> dispatch(@Valid @RequestBody AgentDispatchReq req,
                                                   @AuthenticationPrincipal LoginUser loginUser) {
        return AjaxResult.ok(agentService.dispatch(req, loginUser.getUserId()));
    }

    @Operation(summary = "智能体历史任务")
    @PreAuthorize("hasAuthority('agent:list')")
    @GetMapping("/{agentId}/tasks")
    public AjaxResult<List<QualityAgentTask>> getAgentTasks(@PathVariable Long agentId) {
        return AjaxResult.ok(agentService.getAgentTasks(agentId));
    }

    @Operation(summary = "查询任务被哪些智能体接管")
    @PreAuthorize("hasAuthority('agent:list')")
    @GetMapping("/by-task/{taskId}")
    public AjaxResult<List<QualityAgentTask>> getTaskAgents(@PathVariable Long taskId) {
        return AjaxResult.ok(agentService.getTaskAgents(taskId));
    }
}
