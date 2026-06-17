package com.sva.admin.controller.image;

import com.sva.common.core.domain.AjaxResult;
import com.sva.framework.security.LoginUser;
import com.sva.system.domain.ImageCategory;
import com.sva.system.service.IImageCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "图像分类管理")
@RestController
@RequestMapping("/api/image/categories")
@RequiredArgsConstructor
public class ImageCategoryController {

    private final IImageCategoryService categoryService;

    @Operation(summary = "分类树")
    @GetMapping("/tree")
    public AjaxResult<List<ImageCategory>> tree() {
        return AjaxResult.success(categoryService.tree());
    }

    @Operation(summary = "新增分类")
    @PreAuthorize("hasAuthority('image:category:add')")
    @PostMapping
    public AjaxResult<Void> add(@RequestBody ImageCategory category, @AuthenticationPrincipal LoginUser user) {
        categoryService.add(category, user.getUserId());
        return AjaxResult.success();
    }

    @Operation(summary = "修改分类")
    @PreAuthorize("hasAuthority('image:category:edit')")
    @PutMapping("/{id}")
    public AjaxResult<Void> edit(@PathVariable Long id, @RequestBody ImageCategory category) {
        category.setId(id);
        categoryService.update(category);
        return AjaxResult.success();
    }

    @Operation(summary = "删除分类")
    @PreAuthorize("hasAuthority('image:category:delete')")
    @DeleteMapping("/{id}")
    public AjaxResult<Void> delete(@PathVariable Long id) {
        categoryService.deleteById(id);
        return AjaxResult.success();
    }
}
