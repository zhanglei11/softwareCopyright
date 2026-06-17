package com.sursoft.vision.admin.controller.line;

import com.sursoft.vision.common.core.AjaxResult;
import com.sursoft.vision.common.core.TableDataInfo;
import com.sursoft.vision.system.domain.ProductType;
import com.sursoft.vision.system.dto.ProductTypeDTO;
import com.sursoft.vision.system.service.ProductTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "产品型号管理")
@RequiredArgsConstructor
public class ProductController {

    private final ProductTypeService productService;

    @GetMapping
    @Operation(summary = "查询产品型号列表")
    @PreAuthorize("hasAuthority('product:list')")
    public TableDataInfo<ProductType> list(@RequestParam(value = "lineId", required=false) Long lineId,
            @RequestParam(value = "status", required=false) Integer status,
            @RequestParam(value = "pageNum", defaultValue="1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue="10") int pageSize) {
        return productService.list(lineId, status, pageNum, pageSize);
    }

    @PostMapping
    @Operation(summary = "新增产品型号")
    @PreAuthorize("hasAuthority('product:add')")
    public AjaxResult<Void> add(@Valid @RequestBody ProductTypeDTO dto) {
        productService.add(dto);
        return AjaxResult.success();
    }

    @PutMapping("/{id}")
    @Operation(summary = "编辑产品型号")
    @PreAuthorize("hasAuthority('product:edit')")
    public AjaxResult<Void> edit(@PathVariable Long id, @Valid @RequestBody ProductTypeDTO dto) {
        productService.edit(id, dto);
        return AjaxResult.success();
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "修改型号状态")
    @PreAuthorize("hasAuthority('product:edit')")
    public AjaxResult<Void> updateStatus(@PathVariable Long id, @RequestBody Map<String, Integer> body) {
        productService.updateStatus(id, body.get("status"));
        return AjaxResult.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除产品型号")
    @PreAuthorize("hasAuthority('product:delete')")
    public AjaxResult<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return AjaxResult.success();
    }
}
