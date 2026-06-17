package com.imaging.scheduler.admin.controller.scene;

import com.imaging.scheduler.common.core.AjaxResult;
import com.imaging.scheduler.system.domain.scene.SceneGroup;
import com.imaging.scheduler.system.dto.req.SceneGroupAddReq;
import com.imaging.scheduler.system.service.scene.SceneGroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "场景分组管理")
@RestController
@RequestMapping("/api/v1/scene-groups")
@RequiredArgsConstructor
public class SceneGroupController {

    private final SceneGroupService sceneGroupService;

    @Operation(summary = "场景分组列表")
    @PreAuthorize("hasAuthority('scene:group:list')")
    @GetMapping
    public AjaxResult<List<SceneGroup>> list() {
        return AjaxResult.success(sceneGroupService.getGroupList());
    }

    @Operation(summary = "新增场景分组")
    @PreAuthorize("hasAuthority('scene:group:add')")
    @PostMapping
    public AjaxResult<Void> add(@Valid @RequestBody SceneGroupAddReq req) {
        sceneGroupService.addGroup(req);
        return AjaxResult.success();
    }

    @Operation(summary = "编辑场景分组")
    @PreAuthorize("hasAuthority('scene:group:edit')")
    @PutMapping("/{id}")
    public AjaxResult<Void> edit(@PathVariable("id") Long id, @Valid @RequestBody SceneGroupAddReq req) {
        sceneGroupService.editGroup(id, req);
        return AjaxResult.success();
    }

    @Operation(summary = "删除场景分组")
    @PreAuthorize("hasAuthority('scene:group:delete')")
    @DeleteMapping("/{id}")
    public AjaxResult<Void> delete(@PathVariable("id") Long id) {
        sceneGroupService.deleteGroup(id);
        return AjaxResult.success();
    }
}
