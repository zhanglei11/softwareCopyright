package com.angu.ai.admin.controller.ai;

import com.angu.ai.common.core.domain.AjaxResult;
import com.angu.ai.system.domain.dto.AiModelConfigDTO;
import com.angu.ai.system.domain.entity.AiModelConfig;
import com.angu.ai.system.service.IAiModelConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "AI模型配置")
@RestController
@RequestMapping("/api/ai/models")
@RequiredArgsConstructor
public class ModelConfigController {
    private final IAiModelConfigService modelService;

    @Operation(summary = "模型列表")
    @GetMapping
    public AjaxResult<List<AiModelConfig>> list() { return AjaxResult.success(modelService.list()); }

    @Operation(summary = "新增模型配置")
    @PostMapping
    @PreAuthorize("hasAuthority('ai:model:add')")
    public AjaxResult<Void> create(@RequestBody AiModelConfigDTO dto) { modelService.create(dto); return AjaxResult.success(); }

    @Operation(summary = "编辑模型配置")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ai:model:edit')")
    public AjaxResult<Void> update(@PathVariable Long id, @RequestBody AiModelConfigDTO dto) {
        modelService.update(id, dto); return AjaxResult.success();
    }

    @Operation(summary = "启用/禁用")
    @PutMapping("/{id}/status/{status}")
    @PreAuthorize("hasAuthority('ai:model:edit')")
    public AjaxResult<Void> updateStatus(@PathVariable Long id, @PathVariable Integer status) {
        modelService.updateStatus(id, status); return AjaxResult.success();
    }

    @Operation(summary = "测试连通性")
    @PostMapping("/{id}/test")
    @PreAuthorize("hasAuthority('ai:model:edit')")
    public AjaxResult<Boolean> test(@PathVariable Long id) { return AjaxResult.success(modelService.testConnectivity(id)); }
}
