package com.sursoft.vision.admin.controller.system;

import com.sursoft.vision.common.core.AjaxResult;
import com.sursoft.vision.system.domain.SysMenu;
import com.sursoft.vision.system.dto.MenuDTO;
import com.sursoft.vision.system.service.SysMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/system/menus")
@Tag(name = "菜单管理")
@RequiredArgsConstructor
public class SysMenuController {

    private final SysMenuService menuService;

    @GetMapping("/tree")
    @Operation(summary = "查询菜单树")
    @PreAuthorize("hasAuthority('system:menu:list')")
    public AjaxResult<List<SysMenu>> tree() {
        return AjaxResult.success(menuService.tree());
    }

    @PostMapping
    @Operation(summary = "新增菜单")
    @PreAuthorize("hasAuthority('system:menu:add')")
    public AjaxResult<Void> add(@Valid @RequestBody MenuDTO dto) {
        menuService.add(dto);
        return AjaxResult.success();
    }

    @PutMapping("/{id}")
    @Operation(summary = "编辑菜单")
    @PreAuthorize("hasAuthority('system:menu:edit')")
    public AjaxResult<Void> edit(@PathVariable Long id, @Valid @RequestBody MenuDTO dto) {
        menuService.edit(id, dto);
        return AjaxResult.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除菜单")
    @PreAuthorize("hasAuthority('system:menu:delete')")
    public AjaxResult<Void> delete(@PathVariable Long id) {
        menuService.delete(id);
        return AjaxResult.success();
    }
}
