package com.sursoft.sfd.admin.controller.system;

import com.sursoft.sfd.common.core.AjaxResult;
import com.sursoft.sfd.framework.web.BaseController;
import com.sursoft.sfd.system.domain.SysMenu;
import com.sursoft.sfd.system.service.ISysMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "菜单管理")
@RestController
@RequestMapping("/api/system/menus")
@RequiredArgsConstructor
public class SysMenuController extends BaseController {
    private final ISysMenuService menuService;

    @Operation(summary = "菜单树")
    @GetMapping("/tree")
    public AjaxResult<List<SysMenu>> tree() { return AjaxResult.ok(menuService.tree()); }

    @Operation(summary = "菜单详情")
    @GetMapping("/{id}")
    public AjaxResult<SysMenu> getById(@PathVariable Long id) { return AjaxResult.ok(menuService.getById(id)); }

    @Operation(summary = "新增菜单")
    @PostMapping
    public AjaxResult<Void> add(@RequestBody SysMenu menu) { menuService.add(menu, getCurrentUserId()); return AjaxResult.ok(); }

    @Operation(summary = "编辑菜单")
    @PutMapping("/{id}")
    public AjaxResult<Void> edit(@PathVariable Long id, @RequestBody SysMenu menu) { menuService.edit(id, menu, getCurrentUserId()); return AjaxResult.ok(); }

    @Operation(summary = "删除菜单")
    @DeleteMapping("/{id}")
    public AjaxResult<Void> delete(@PathVariable Long id) { menuService.delete(id); return AjaxResult.ok(); }
}
