package com.sursoft.sfd.admin.controller.system;

import com.sursoft.sfd.common.core.AjaxResult;
import com.sursoft.sfd.framework.web.BaseController;
import com.sursoft.sfd.system.domain.SysRole;
import com.sursoft.sfd.system.service.ISysRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "角色管理")
@RestController
@RequestMapping("/api/system/roles")
@RequiredArgsConstructor
public class SysRoleController extends BaseController {
    private final ISysRoleService roleService;

    @Operation(summary = "角色列表")
    @GetMapping
    public AjaxResult<List<SysRole>> list(@RequestParam(required = false) String keyword,
                                          @RequestParam(required = false) Integer status) {
        return AjaxResult.ok(roleService.list(keyword, status));
    }

    @Operation(summary = "角色详情")
    @GetMapping("/{id}")
    public AjaxResult<SysRole> getById(@PathVariable Long id) { return AjaxResult.ok(roleService.getById(id)); }

    @Operation(summary = "新增角色")
    @PostMapping
    public AjaxResult<Void> add(@RequestBody SysRole role) { roleService.add(role, getCurrentUserId()); return AjaxResult.ok(); }

    @Operation(summary = "编辑角色")
    @PutMapping("/{id}")
    public AjaxResult<Void> edit(@PathVariable Long id, @RequestBody SysRole role) { roleService.edit(id, role, getCurrentUserId()); return AjaxResult.ok(); }

    @Operation(summary = "删除角色")
    @DeleteMapping("/{id}")
    public AjaxResult<Void> delete(@PathVariable Long id) { roleService.delete(id); return AjaxResult.ok(); }

    @Operation(summary = "分配菜单权限")
    @PutMapping("/{id}/menus")
    public AjaxResult<Void> assignMenus(@PathVariable Long id, @RequestBody List<Long> menuIds) { roleService.assignMenus(id, menuIds); return AjaxResult.ok(); }

    @Operation(summary = "获取角色菜单ID列表")
    @GetMapping("/{id}/menus")
    public AjaxResult<List<Long>> getMenuIds(@PathVariable Long id) { return AjaxResult.ok(roleService.getMenuIds(id)); }
}
