package com.sursoft.iidp.system.sys.controller;

import com.sursoft.iidp.common.core.AjaxResult;
import com.sursoft.iidp.system.sys.domain.SysMenu;
import com.sursoft.iidp.system.sys.service.SysMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "菜单管理")
@RestController
@RequestMapping("/api/system/menus")
@RequiredArgsConstructor
public class SysMenuController {
    private final SysMenuService menuService;

    @Operation(summary = "菜单树")
    @PreAuthorize("hasAuthority('system:menu:list')")
    @GetMapping
    public AjaxResult<List<SysMenu>> list() {
        return AjaxResult.success(menuService.listMenus());
    }

    @Operation(summary = "新增菜单")
    @PreAuthorize("hasAuthority('system:menu:add')")
    @PostMapping
    public AjaxResult<Void> add(@RequestBody SysMenu menu) {
        menuService.addMenu(menu);
        return AjaxResult.success();
    }

    @Operation(summary = "修改菜单")
    @PreAuthorize("hasAuthority('system:menu:edit')")
    @PutMapping("/{id}")
    public AjaxResult<Void> edit(@PathVariable Long id, @RequestBody SysMenu menu) {
        menu.setId(id);
        menuService.editMenu(menu);
        return AjaxResult.success();
    }

    @Operation(summary = "删除菜单")
    @PreAuthorize("hasAuthority('system:menu:delete')")
    @DeleteMapping("/{id}")
    public AjaxResult<Void> remove(@PathVariable Long id) {
        menuService.removeMenu(id);
        return AjaxResult.success();
    }
}
