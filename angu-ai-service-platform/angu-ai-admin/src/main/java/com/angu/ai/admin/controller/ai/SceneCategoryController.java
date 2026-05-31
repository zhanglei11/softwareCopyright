package com.angu.ai.admin.controller.ai;

import com.angu.ai.common.core.domain.AjaxResult;
import com.angu.ai.system.domain.entity.AiSceneCategory;
import com.angu.ai.system.service.IAiSceneCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "场景分类管理")
@RestController
@RequestMapping("/api/ai/categories")
@RequiredArgsConstructor
public class SceneCategoryController {
    private final IAiSceneCategoryService categoryService;

    @Operation(summary = "分类列表")
    @GetMapping
    public AjaxResult<List<AiSceneCategory>> list() { return AjaxResult.success(categoryService.list()); }

    @Operation(summary = "新增分类")
    @PostMapping
    @PreAuthorize("hasAuthority('ai:category:add')")
    public AjaxResult<Void> create(@RequestBody AiSceneCategory category) { categoryService.create(category); return AjaxResult.success(); }

    @Operation(summary = "编辑分类")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ai:category:edit')")
    public AjaxResult<Void> update(@PathVariable Long id, @RequestBody AiSceneCategory category) {
        categoryService.update(id, category); return AjaxResult.success();
    }

    @Operation(summary = "删除分类")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ai:category:delete')")
    public AjaxResult<Void> delete(@PathVariable Long id) { categoryService.deleteById(id); return AjaxResult.success(); }
}
