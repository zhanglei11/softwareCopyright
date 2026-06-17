package com.angu.matcher.admin.controller;

import com.angu.matcher.common.result.AjaxResult;
import com.angu.matcher.framework.security.LoginUser;
import com.angu.matcher.framework.web.BaseController;
import com.angu.matcher.system.domain.SysMenu;
import com.angu.matcher.system.service.ISysMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "菜单管理")
@RestController
@RequestMapping("/api/system/menus")
@RequiredArgsConstructor
public class SysMenuController extends BaseController {

    private final ISysMenuService menuService;

    @Operation(summary = "菜单树")
    @PreAuthorize("hasAuthority('system:menu:list')")
    @GetMapping("/tree")
    public AjaxResult<?> tree() {
        return AjaxResult.success(menuService.getMenuTree());
    }

    @Operation(summary = "当前用户菜单树")
    @GetMapping("/my-tree")
    public AjaxResult<?> myTree(@AuthenticationPrincipal LoginUser lu) {
        return AjaxResult.success(menuService.getMenuTreeByUserId(lu.getUserId()));
    }

    @Operation(summary = "新增菜单")
    @PreAuthorize("hasAuthority('system:menu:add')")
    @PostMapping
    public AjaxResult<Void> create(@RequestBody SysMenu menu) {
        menuService.createMenu(menu);
        return AjaxResult.success();
    }

    @Operation(summary = "编辑菜单")
    @PreAuthorize("hasAuthority('system:menu:edit')")
    @PutMapping("/{id}")
    public AjaxResult<Void> update(@PathVariable Long id, @RequestBody SysMenu menu) {
        menu.setId(id);
        menuService.updateMenu(menu);
        return AjaxResult.success();
    }

    @Operation(summary = "删除菜单")
    @PreAuthorize("hasAuthority('system:menu:delete')")
    @DeleteMapping("/{id}")
    public AjaxResult<Void> delete(@PathVariable Long id) {
        menuService.deleteMenu(id);
        return AjaxResult.success();
    }
}
