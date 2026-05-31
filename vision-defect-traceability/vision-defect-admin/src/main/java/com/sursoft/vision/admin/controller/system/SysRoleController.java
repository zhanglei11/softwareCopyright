package com.sursoft.vision.admin.controller.system;

import com.sursoft.vision.common.core.AjaxResult;
import com.sursoft.vision.common.core.TableDataInfo;
import com.sursoft.vision.system.domain.SysRole;
import com.sursoft.vision.system.dto.RoleDTO;
import com.sursoft.vision.system.service.SysRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/system/roles")
@Tag(name = "角色管理")
@RequiredArgsConstructor
public class SysRoleController {

    private final SysRoleService roleService;

    @GetMapping
    @Operation(summary = "查询角色列表")
    @PreAuthorize("hasAuthority('system:role:list')")
    public TableDataInfo<SysRole> list(@RequestParam(value = "roleName", required=false) String roleName,
            @RequestParam(value = "status", required=false) Integer status,
            @RequestParam(value = "pageNum", defaultValue="1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue="10") int pageSize) {
        return roleService.list(roleName, status, pageNum, pageSize);
    }

    @PostMapping
    @Operation(summary = "新增角色")
    @PreAuthorize("hasAuthority('system:role:add')")
    public AjaxResult<Void> add(@Valid @RequestBody RoleDTO dto) {
        roleService.add(dto);
        return AjaxResult.success();
    }

    @PutMapping("/{id}")
    @Operation(summary = "编辑角色")
    @PreAuthorize("hasAuthority('system:role:edit')")
    public AjaxResult<Void> edit(@PathVariable Long id, @Valid @RequestBody RoleDTO dto) {
        roleService.edit(id, dto);
        return AjaxResult.success();
    }

    @PutMapping("/{id}/menus")
    @Operation(summary = "角色授权菜单")
    @PreAuthorize("hasAuthority('system:role:edit')")
    public AjaxResult<Void> assignMenus(@PathVariable Long id, @RequestBody Map<String, List<Long>> body) {
        roleService.assignMenus(id, body.get("menuIds"));
        return AjaxResult.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除角色")
    @PreAuthorize("hasAuthority('system:role:delete')")
    public AjaxResult<Void> delete(@PathVariable Long id) {
        roleService.delete(id);
        return AjaxResult.success();
    }
}
