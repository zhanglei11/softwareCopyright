package com.angu.ai.admin.controller.app;

import com.angu.ai.common.core.domain.AjaxResult;
import com.angu.ai.common.utils.SecurityUtils;
import com.angu.ai.system.domain.vo.SceneVO;
import com.angu.ai.system.service.IAiSceneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "应用端-场景")
@RestController
@RequestMapping("/api/app/scenes")
@RequiredArgsConstructor
public class SceneAppController {
    private final IAiSceneService sceneService;

    @Operation(summary = "获取可用场景列表")
    @GetMapping
    public AjaxResult<List<SceneVO>> list(@RequestParam(required = false) String keyword) {
        return AjaxResult.success(sceneService.getOnlineByUser(SecurityUtils.getUserId(), keyword));
    }

    @Operation(summary = "场景详情")
    @GetMapping("/{id}")
    public AjaxResult<SceneVO> get(@PathVariable Long id) { return AjaxResult.success(sceneService.getById(id)); }

    @Operation(summary = "收藏场景")
    @PostMapping("/{id}/favorite")
    public AjaxResult<Void> favorite(@PathVariable Long id) {
        sceneService.addFavorite(SecurityUtils.getUserId(), id); return AjaxResult.success();
    }

    @Operation(summary = "取消收藏")
    @DeleteMapping("/{id}/favorite")
    public AjaxResult<Void> unfavorite(@PathVariable Long id) {
        sceneService.removeFavorite(SecurityUtils.getUserId(), id); return AjaxResult.success();
    }

    @Operation(summary = "我的收藏")
    @GetMapping("/favorites")
    public AjaxResult<List<SceneVO>> favorites() {
        return AjaxResult.success(sceneService.getFavorites(SecurityUtils.getUserId()));
    }
}
