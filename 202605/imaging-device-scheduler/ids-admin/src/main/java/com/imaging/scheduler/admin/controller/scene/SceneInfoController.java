package com.imaging.scheduler.admin.controller.scene;

import com.imaging.scheduler.common.core.AjaxResult;
import com.imaging.scheduler.common.core.TableDataInfo;
import com.imaging.scheduler.framework.web.BaseController;
import com.imaging.scheduler.system.domain.scene.SceneInfo;
import com.imaging.scheduler.system.dto.req.SceneAddReq;
import com.imaging.scheduler.system.dto.req.SceneQueryReq;
import com.imaging.scheduler.system.service.scene.SceneInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "场景管理")
@RestController
@RequestMapping("/api/v1/scenes")
@RequiredArgsConstructor
public class SceneInfoController extends BaseController {

    private final SceneInfoService sceneInfoService;

    @Operation(summary = "场景分页列表")
    @PreAuthorize("hasAuthority('scene:info:list')")
    @GetMapping
    public TableDataInfo<SceneInfo> list(SceneQueryReq req) {
        return sceneInfoService.getSceneList(req);
    }

    @Operation(summary = "场景详情")
    @PreAuthorize("hasAuthority('scene:info:list')")
    @GetMapping("/{id}")
    public AjaxResult<SceneInfo> detail(@PathVariable("id") Long id) {
        return AjaxResult.success(sceneInfoService.getSceneById(id));
    }

    @Operation(summary = "新增场景")
    @PreAuthorize("hasAuthority('scene:info:add')")
    @PostMapping
    public AjaxResult<Void> add(@Valid @RequestBody SceneAddReq req) {
        sceneInfoService.addScene(req);
        return AjaxResult.success();
    }

    @Operation(summary = "编辑场景")
    @PreAuthorize("hasAuthority('scene:info:edit')")
    @PutMapping("/{id}")
    public AjaxResult<Void> edit(@PathVariable("id") Long id, @Valid @RequestBody SceneAddReq req) {
        sceneInfoService.editScene(id, req);
        return AjaxResult.success();
    }

    @Operation(summary = "切换场景状态")
    @PreAuthorize("hasAuthority('scene:info:edit')")
    @PatchMapping("/{id}/status")
    public AjaxResult<Void> updateStatus(@PathVariable("id") Long id, @RequestBody Map<String, Integer> body) {
        sceneInfoService.updateStatus(id, body.get("status"));
        return AjaxResult.success();
    }

    @Operation(summary = "删除场景")
    @PreAuthorize("hasAuthority('scene:info:delete')")
    @DeleteMapping("/{id}")
    public AjaxResult<Void> delete(@PathVariable("id") Long id) {
        sceneInfoService.deleteScene(id);
        return AjaxResult.success();
    }
}
