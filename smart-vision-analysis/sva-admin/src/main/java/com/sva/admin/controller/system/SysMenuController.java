package com.sva.admin.controller.system;

import com.sva.common.core.domain.AjaxResult;
import com.sva.framework.security.LoginUser;
import com.sva.system.domain.SysMenu;
import com.sva.system.service.ISysMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "菜单管理")
@RestController
@RequestMapping("/api/system/menus")
@RequiredArgsConstructor
public class SysMenuController {

    private final ISysMenuService menuService;

    @Operation(summary = "菜单树（当前用户）")
    @GetMapping("/tree")
    public AjaxResult<List<SysMenu>> tree(@AuthenticationPrincipal LoginUser user) {
        return AjaxResult.success(menuService.treeByUserId(user.getUserId()));
    }

    @Operation(summary = "全量菜单树（管理员）")
    @PreAuthorize("hasAuthority('system:menu:list')")
    @GetMapping("/all")
    public AjaxResult<List<SysMenu>> allTree() {
        return AjaxResult.success(menuService.allTree());
    }

    @Operation(summary = "新增菜单")
    @PreAuthorize("hasAuthority('system:menu:add')")
    @PostMapping
    public AjaxResult<Void> add(@RequestBody SysMenu menu) {
        menuService.add(menu);
        return AjaxResult.success();
    }

    @Operation(summary = "修改菜单")
    @PreAuthorize("hasAuthority('system:menu:edit')")
    @PutMapping("/{id}")
    public AjaxResult<Void> edit(@PathVariable Long id, @RequestBody SysMenu menu) {
        menu.setId(id);
        menuService.update(menu);
        return AjaxResult.success();
    }

    @Operation(summary = "删除菜单")
    @PreAuthorize("hasAuthority('system:menu:delete')")
    @DeleteMapping("/{id}")
    public AjaxResult<Void> delete(@PathVariable Long id) {
        menuService.deleteById(id);
        return AjaxResult.success();
    }
}
