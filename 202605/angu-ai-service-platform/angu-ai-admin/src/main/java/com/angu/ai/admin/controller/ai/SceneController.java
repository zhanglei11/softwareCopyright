package com.angu.ai.admin.controller.ai;

import com.angu.ai.common.core.domain.AjaxResult;
import com.angu.ai.common.core.page.TableDataInfo;
import com.angu.ai.common.utils.SecurityUtils;
import com.angu.ai.system.domain.dto.AiSceneDTO;
import com.angu.ai.system.domain.query.SceneQuery;
import com.angu.ai.system.domain.vo.SceneVO;
import com.angu.ai.system.service.IAiSceneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "AI场景管理")
@RestController
@RequestMapping("/api/ai/scenes")
@RequiredArgsConstructor
public class SceneController {
    private final IAiSceneService sceneService;

    @Operation(summary = "场景分页列表")
    @GetMapping
    @PreAuthorize("hasAuthority('ai:scene:list')")
    public AjaxResult<TableDataInfo<SceneVO>> page(SceneQuery query) { return AjaxResult.success(sceneService.pageList(query)); }

    @Operation(summary = "场景详情")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ai:scene:query')")
    public AjaxResult<SceneVO> get(@PathVariable Long id) { return AjaxResult.success(sceneService.getById(id)); }

    @Operation(summary = "新增场景")
    @PostMapping
    @PreAuthorize("hasAuthority('ai:scene:add')")
    public AjaxResult<Void> create(@RequestBody AiSceneDTO dto) {
        sceneService.create(dto, SecurityUtils.getUserId()); return AjaxResult.success();
    }

    @Operation(summary = "编辑场景")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ai:scene:edit')")
    public AjaxResult<Void> update(@PathVariable Long id, @RequestBody AiSceneDTO dto) {
        sceneService.update(id, dto); return AjaxResult.success();
    }

    @Operation(summary = "删除场景")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ai:scene:delete')")
    public AjaxResult<Void> delete(@PathVariable Long id) { sceneService.deleteById(id); return AjaxResult.success(); }

    @Operation(summary = "发布场景")
    @PutMapping("/{id}/publish")
    @PreAuthorize("hasAuthority('ai:scene:edit')")
    public AjaxResult<Void> publish(@PathVariable Long id) { sceneService.publish(id); return AjaxResult.success(); }

    @Operation(summary = "下线场景")
    @PutMapping("/{id}/offline")
    @PreAuthorize("hasAuthority('ai:scene:edit')")
    public AjaxResult<Void> offline(@PathVariable Long id) { sceneService.offline(id); return AjaxResult.success(); }
}
