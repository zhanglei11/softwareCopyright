package com.angu.matcher.admin.controller;

import com.angu.matcher.common.result.AjaxResult;
import com.angu.matcher.framework.web.BaseController;
import com.angu.matcher.system.domain.SysRole;
import com.angu.matcher.system.dto.RoleMenuRequest;
import com.angu.matcher.system.service.ISysRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "角色管理")
@RestController
@RequestMapping("/api/system/roles")
@RequiredArgsConstructor
public class SysRoleController extends BaseController {

    private final ISysRoleService roleService;

    @Operation(summary = "角色列表")
    @PreAuthorize("hasAuthority('system:role:list')")
    @GetMapping
    public AjaxResult<?> list() {
        return AjaxResult.success(roleService.listRoles());
    }

    @Operation(summary = "新增角色")
    @PreAuthorize("hasAuthority('system:role:add')")
    @PostMapping
    public AjaxResult<Void> create(@RequestBody SysRole role) {
        roleService.createRole(role);
        return AjaxResult.success();
    }

    @Operation(summary = "编辑角色")
    @PreAuthorize("hasAuthority('system:role:edit')")
    @PutMapping("/{id}")
    public AjaxResult<Void> update(@PathVariable Long id, @RequestBody SysRole role) {
        role.setId(id);
        roleService.updateRole(role);
        return AjaxResult.success();
    }

    @Operation(summary = "删除角色")
    @PreAuthorize("hasAuthority('system:role:delete')")
    @DeleteMapping("/{id}")
    public AjaxResult<Void> delete(@PathVariable Long id) {
        roleService.deleteRole(id);
        return AjaxResult.success();
    }

    @Operation(summary = "角色授权菜单")
    @PreAuthorize("hasAuthority('system:role:edit')")
    @PutMapping("/{id}/menus")
    public AjaxResult<Void> assignMenus(@PathVariable Long id, @RequestBody RoleMenuRequest req) {
        roleService.assignMenus(id, req.getMenuIds());
        return AjaxResult.success();
    }
}
