package com.vqcc.admin.controller.system;

import com.vqcc.common.result.AjaxResult;
import com.vqcc.system.domain.SysMenu;
import com.vqcc.system.service.ISysMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "菜单管理")
@RestController
@RequestMapping("/api/v1/system/menus")
@RequiredArgsConstructor
public class SysMenuController {

    private final ISysMenuService menuService;

    @Operation(summary = "菜单树")
    @PreAuthorize("hasAuthority('system:menu:list')")
    @GetMapping("/tree")
    public AjaxResult<List<SysMenu>> tree() {
        return AjaxResult.ok(menuService.tree());
    }

    @Operation(summary = "创建菜单")
    @PreAuthorize("hasAuthority('system:menu:add')")
    @PostMapping
    public AjaxResult<Void> create(@RequestBody SysMenu menu) {
        menuService.create(menu);
        return AjaxResult.ok(null);
    }

    @Operation(summary = "更新菜单")
    @PreAuthorize("hasAuthority('system:menu:edit')")
    @PutMapping("/{id}")
    public AjaxResult<Void> update(@PathVariable Long id, @RequestBody SysMenu menu) {
        menu.setId(id);
        menuService.update(menu);
        return AjaxResult.ok(null);
    }

    @Operation(summary = "删除菜单")
    @PreAuthorize("hasAuthority('system:menu:delete')")
    @DeleteMapping("/{id}")
    public AjaxResult<Void> delete(@PathVariable Long id) {
        menuService.delete(id);
        return AjaxResult.ok(null);
    }
}
