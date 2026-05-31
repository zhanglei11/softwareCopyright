package com.sursoft.vision.admin.controller.defect;

import com.sursoft.vision.common.core.AjaxResult;
import com.sursoft.vision.common.core.TableDataInfo;
import com.sursoft.vision.system.domain.DefectCategory;
import com.sursoft.vision.system.dto.DefectCategoryDTO;
import com.sursoft.vision.system.service.DefectCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/defect/categories")
@Tag(name = "缺陷分类管理")
@RequiredArgsConstructor
public class DefectCategoryController {

    private final DefectCategoryService categoryService;

    @GetMapping
    @Operation(summary = "查询缺陷分类列表")
    @PreAuthorize("hasAuthority('defect:category:list')")
    public TableDataInfo<DefectCategory> list(@RequestParam(value = "name", required=false) String name,
            @RequestParam(value = "level", required=false) Integer level,
            @RequestParam(value = "status", required=false) Integer status,
            @RequestParam(value = "pageNum", defaultValue="1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue="10") int pageSize) {
        return categoryService.list(name, level, status, pageNum, pageSize);
    }

    @PostMapping
    @Operation(summary = "新增缺陷分类")
    @PreAuthorize("hasAuthority('defect:category:add')")
    public AjaxResult<Void> add(@Valid @RequestBody DefectCategoryDTO dto) {
        categoryService.add(dto);
        return AjaxResult.success();
    }

    @PutMapping("/{id}")
    @Operation(summary = "编辑缺陷分类")
    @PreAuthorize("hasAuthority('defect:category:edit')")
    public AjaxResult<Void> edit(@PathVariable Long id, @Valid @RequestBody DefectCategoryDTO dto) {
        categoryService.edit(id, dto);
        return AjaxResult.success();
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "修改分类状态")
    @PreAuthorize("hasAuthority('defect:category:edit')")
    public AjaxResult<Void> updateStatus(@PathVariable Long id, @RequestBody Map<String, Integer> body) {
        categoryService.updateStatus(id, body.get("status"));
        return AjaxResult.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除缺陷分类")
    @PreAuthorize("hasAuthority('defect:category:delete')")
    public AjaxResult<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return AjaxResult.success();
    }
}
