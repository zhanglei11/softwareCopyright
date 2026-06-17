package com.sursoft.iidp.system.sys.controller;

import com.sursoft.iidp.common.core.AjaxResult;
import com.sursoft.iidp.common.core.PageDomain;
import com.sursoft.iidp.common.core.TableDataInfo;
import com.sursoft.iidp.framework.web.BaseController;
import com.sursoft.iidp.system.sys.domain.SysRole;
import com.sursoft.iidp.system.sys.service.SysRoleService;
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
    private final SysRoleService roleService;

    @Operation(summary = "角色列表")
    @PreAuthorize("hasAuthority('system:role:list')")
    @GetMapping
    public TableDataInfo<SysRole> list(SysRole query, PageDomain page) {
        startPage(page);
        return getDataTable(roleService.listRoles(query));
    }

    @Operation(summary = "角色详情")
    @GetMapping("/{id}")
    public AjaxResult<SysRole> getInfo(@PathVariable Long id) {
        return AjaxResult.success(roleService.getRoleById(id));
    }

    @Operation(summary = "新增角色")
    @PreAuthorize("hasAuthority('system:role:add')")
    @PostMapping
    public AjaxResult<Void> add(@RequestBody SysRole role) {
        roleService.addRole(role);
        return AjaxResult.success();
    }

    @Operation(summary = "修改角色")
    @PreAuthorize("hasAuthority('system:role:edit')")
    @PutMapping("/{id}")
    public AjaxResult<Void> edit(@PathVariable Long id, @RequestBody SysRole role) {
        role.setId(id);
        roleService.editRole(role);
        return AjaxResult.success();
    }

    @Operation(summary = "删除角色")
    @PreAuthorize("hasAuthority('system:role:delete')")
    @DeleteMapping("/{id}")
    public AjaxResult<Void> remove(@PathVariable Long id) {
        roleService.removeRole(id);
        return AjaxResult.success();
    }
}
