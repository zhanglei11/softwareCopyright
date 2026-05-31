package com.vqcc.admin.controller.system;

import com.vqcc.common.result.AjaxResult;
import com.vqcc.common.result.TableDataInfo;
import com.vqcc.framework.security.LoginUser;
import com.vqcc.system.domain.SysRole;
import com.vqcc.system.service.ISysRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@Tag(name = "角色管理")
@RestController
@RequestMapping("/api/v1/system/roles")
@RequiredArgsConstructor
public class SysRoleController {

    private final ISysRoleService roleService;

    @Operation(summary = "角色列表")
    @PreAuthorize("hasAuthority('system:role:list')")
    @GetMapping
    public AjaxResult<TableDataInfo<SysRole>> list(
            @RequestParam(required = false) String roleName,
            @RequestParam(required = false) Integer status) {
        return AjaxResult.ok(TableDataInfo.ok(roleService.list(roleName, status)));
    }

    @Operation(summary = "角色详情")
    @PreAuthorize("hasAuthority('system:role:list')")
    @GetMapping("/{id}")
    public AjaxResult<SysRole> getById(@PathVariable Long id) {
        return AjaxResult.ok(roleService.getById(id));
    }

    @Operation(summary = "创建角色")
    @PreAuthorize("hasAuthority('system:role:add')")
    @PostMapping
    public AjaxResult<Void> create(@RequestBody SysRole role, @AuthenticationPrincipal LoginUser loginUser) {
        roleService.create(role, loginUser.getUserId());
        return AjaxResult.ok(null);
    }

    @Operation(summary = "更新角色")
    @PreAuthorize("hasAuthority('system:role:edit')")
    @PutMapping("/{id}")
    public AjaxResult<Void> update(@PathVariable Long id, @RequestBody SysRole role,
                                    @AuthenticationPrincipal LoginUser loginUser) {
        role.setId(id);
        roleService.update(role, loginUser.getUserId());
        return AjaxResult.ok(null);
    }

    @Operation(summary = "删除角色")
    @PreAuthorize("hasAuthority('system:role:delete')")
    @DeleteMapping("/{id}")
    public AjaxResult<Void> delete(@PathVariable Long id) {
        roleService.delete(id);
        return AjaxResult.ok(null);
    }

    @Operation(summary = "分配菜单权限")
    @PreAuthorize("hasAuthority('system:role:edit')")
    @PutMapping("/{id}/menus")
    public AjaxResult<Void> assignMenus(@PathVariable Long id, @RequestBody Map<String, List<Long>> body) {
        roleService.assignMenus(id, body.get("menuIds"));
        return AjaxResult.ok(null);
    }

    @Operation(summary = "获取角色已分配的菜单ID")
    @PreAuthorize("hasAuthority('system:role:list')")
    @GetMapping("/{id}/menus")
    public AjaxResult<List<Long>> getMenuIds(@PathVariable Long id) {
        return AjaxResult.ok(roleService.getMenuIds(id));
    }
}
