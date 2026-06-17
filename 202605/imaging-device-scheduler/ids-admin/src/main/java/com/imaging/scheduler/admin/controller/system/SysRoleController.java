package com.imaging.scheduler.admin.controller.system;

import com.imaging.scheduler.common.core.AjaxResult;
import com.imaging.scheduler.system.domain.system.SysRole;
import com.imaging.scheduler.system.dto.req.RoleAddReq;
import com.imaging.scheduler.system.dto.req.RoleMenuReq;
import com.imaging.scheduler.system.service.system.SysRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "角色管理")
@RestController
@RequestMapping("/api/v1/system/roles")
@RequiredArgsConstructor
public class SysRoleController {

    private final SysRoleService roleService;

    @Operation(summary = "角色列表")
    @PreAuthorize("hasAuthority('system:role:list')")
    @GetMapping
    public AjaxResult<List<SysRole>> list() {
        return AjaxResult.success(roleService.getRoleList());
    }

    @Operation(summary = "新增角色")
    @PreAuthorize("hasAuthority('system:role:add')")
    @PostMapping
    public AjaxResult<Void> add(@Valid @RequestBody RoleAddReq req) {
        roleService.addRole(req);
        return AjaxResult.success();
    }

    @Operation(summary = "编辑角色")
    @PreAuthorize("hasAuthority('system:role:edit')")
    @PutMapping("/{id}")
    public AjaxResult<Void> edit(@PathVariable("id") Long id, @Valid @RequestBody RoleAddReq req) {
        roleService.editRole(id, req);
        return AjaxResult.success();
    }

    @Operation(summary = "角色授权菜单")
    @PreAuthorize("hasAuthority('system:role:edit')")
    @PutMapping("/{id}/menus")
    public AjaxResult<Void> assignMenus(@PathVariable("id") Long id, @RequestBody RoleMenuReq req) {
        roleService.assignMenus(id, req);
        return AjaxResult.success();
    }

    @Operation(summary = "删除角色")
    @PreAuthorize("hasAuthority('system:role:delete')")
    @DeleteMapping("/{id}")
    public AjaxResult<Void> delete(@PathVariable("id") Long id) {
        roleService.deleteRole(id);
        return AjaxResult.success();
    }
}
