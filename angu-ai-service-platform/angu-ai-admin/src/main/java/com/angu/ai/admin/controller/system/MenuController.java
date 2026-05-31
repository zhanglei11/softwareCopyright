package com.angu.ai.admin.controller.system;

import com.angu.ai.common.core.domain.AjaxResult;
import com.angu.ai.system.domain.dto.MenuDTO;
import com.angu.ai.system.domain.entity.SysMenu;
import com.angu.ai.system.service.ISysMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "菜单管理")
@RestController
@RequestMapping("/api/system/menus")
@RequiredArgsConstructor
public class MenuController {
    private final ISysMenuService menuService;

    @Operation(summary = "菜单树")
    @GetMapping("/tree")
    public AjaxResult<List<SysMenu>> tree() { return AjaxResult.success(menuService.getTree()); }

    @Operation(summary = "新增菜单")
    @PostMapping
    @PreAuthorize("hasAuthority('system:menu:add')")
    public AjaxResult<Void> create(@RequestBody MenuDTO dto) { menuService.create(dto); return AjaxResult.success(); }

    @Operation(summary = "编辑菜单")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('system:menu:edit')")
    public AjaxResult<Void> update(@PathVariable Long id, @RequestBody MenuDTO dto) {
        menuService.update(id, dto); return AjaxResult.success();
    }

    @Operation(summary = "删除菜单")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('system:menu:delete')")
    public AjaxResult<Void> delete(@PathVariable Long id) { menuService.deleteById(id); return AjaxResult.success(); }
}
