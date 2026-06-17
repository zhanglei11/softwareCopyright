package com.angu.ai.admin.controller.system;

import com.angu.ai.common.core.domain.AjaxResult;
import com.angu.ai.system.domain.dto.RoleDTO;
import com.angu.ai.system.domain.vo.RoleVO;
import com.angu.ai.system.service.ISysRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@Tag(name = "角色管理")
@RestController
@RequestMapping("/api/system/roles")
@RequiredArgsConstructor
public class RoleController {
    private final ISysRoleService roleService;

    @Operation(summary = "角色列表")
    @GetMapping
    public AjaxResult<List<RoleVO>> list() { return AjaxResult.success(roleService.list()); }

    @Operation(summary = "新增角色")
    @PostMapping
    @PreAuthorize("hasAuthority('system:role:add')")
    public AjaxResult<Void> create(@RequestBody RoleDTO dto) { roleService.create(dto); return AjaxResult.success(); }

    @Operation(summary = "编辑角色")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('system:role:edit')")
    public AjaxResult<Void> update(@PathVariable Long id, @RequestBody RoleDTO dto) {
        roleService.update(id, dto); return AjaxResult.success();
    }

    @Operation(summary = "删除角色")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('system:role:delete')")
    public AjaxResult<Void> delete(@PathVariable Long id) { roleService.deleteById(id); return AjaxResult.success(); }

    @Operation(summary = "分配菜单")
    @PutMapping("/{id}/menus")
    @PreAuthorize("hasAuthority('system:role:edit')")
    public AjaxResult<Void> assignMenus(@PathVariable Long id, @RequestBody Map<String, List<Long>> body) {
        roleService.assignMenus(id, body.get("menuIds")); return AjaxResult.success();
    }

    @Operation(summary = "分配场景")
    @PutMapping("/{id}/scenes")
    @PreAuthorize("hasAuthority('system:role:edit')")
    public AjaxResult<Void> assignScenes(@PathVariable Long id, @RequestBody Map<String, List<Long>> body) {
        roleService.assignScenes(id, body.get("sceneIds")); return AjaxResult.success();
    }
}
