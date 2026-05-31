package com.sva.admin.controller.system;

import com.sva.common.core.controller.BaseController;
import com.sva.common.core.domain.AjaxResult;
import com.sva.common.core.domain.TableDataInfo;
import com.sva.framework.security.LoginUser;
import com.sva.system.domain.SysRole;
import com.sva.system.service.ISysRoleService;
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
@RequestMapping("/api/system/roles")
@RequiredArgsConstructor
public class SysRoleController extends BaseController {

    private final ISysRoleService roleService;

    @Operation(summary = "角色列表")
    @PreAuthorize("hasAuthority('system:role:list')")
    @GetMapping
    public TableDataInfo list(@RequestParam(required = false) String roleName,
                              @RequestParam(required = false) Integer status,
                              @RequestParam(defaultValue = "1") int pageNum,
                              @RequestParam(defaultValue = "10") int pageSize) {
        startPage(pageNum, pageSize);
        return getDataTable(roleService.list(roleName, status));
    }

    @Operation(summary = "角色详情")
    @GetMapping("/{id}")
    public AjaxResult<SysRole> getInfo(@PathVariable Long id) {
        return AjaxResult.success(roleService.getById(id));
    }

    @Operation(summary = "新增角色")
    @PreAuthorize("hasAuthority('system:role:add')")
    @PostMapping
    public AjaxResult<Void> add(@RequestBody SysRole role, @AuthenticationPrincipal LoginUser user) {
        roleService.add(role, user.getUserId());
        return AjaxResult.success();
    }

    @Operation(summary = "修改角色")
    @PreAuthorize("hasAuthority('system:role:edit')")
    @PutMapping("/{id}")
    public AjaxResult<Void> edit(@PathVariable Long id, @RequestBody SysRole role,
                                 @AuthenticationPrincipal LoginUser user) {
        role.setId(id);
        roleService.update(role, user.getUserId());
        return AjaxResult.success();
    }

    @Operation(summary = "删除角色")
    @PreAuthorize("hasAuthority('system:role:delete')")
    @DeleteMapping("/{id}")
    public AjaxResult<Void> delete(@PathVariable Long id) {
        roleService.deleteById(id);
        return AjaxResult.success();
    }

    @Operation(summary = "角色已分配菜单ID列表")
    @GetMapping("/{id}/menus")
    public AjaxResult<List<Long>> getMenuIds(@PathVariable Long id) {
        return AjaxResult.success(roleService.getMenuIds(id));
    }

    @Operation(summary = "分配菜单权限")
    @PreAuthorize("hasAuthority('system:role:edit')")
    @PostMapping("/{id}/menus")
    public AjaxResult<Void> assignMenus(@PathVariable Long id, @RequestBody Map<String, List<Long>> body) {
        roleService.assignMenus(id, body.get("menuIds"));
        return AjaxResult.success();
    }
}
