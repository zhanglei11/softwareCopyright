package com.imaging.scheduler.admin.controller.system;

import com.imaging.scheduler.common.core.AjaxResult;
import com.imaging.scheduler.system.domain.system.SysMenu;
import com.imaging.scheduler.system.dto.req.MenuAddReq;
import com.imaging.scheduler.system.service.system.SysMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "菜单管理")
@RestController
@RequestMapping("/api/v1/system/menus")
@RequiredArgsConstructor
public class SysMenuController {

    private final SysMenuService menuService;

    @Operation(summary = "菜单树")
    @PreAuthorize("hasAuthority('system:menu:list')")
    @GetMapping("/tree")
    public AjaxResult<List<SysMenu>> tree() {
        return AjaxResult.success(menuService.getMenuTree());
    }

    @Operation(summary = "新增菜单")
    @PreAuthorize("hasAuthority('system:menu:add')")
    @PostMapping
    public AjaxResult<Void> add(@Valid @RequestBody MenuAddReq req) {
        menuService.addMenu(req);
        return AjaxResult.success();
    }

    @Operation(summary = "编辑菜单")
    @PreAuthorize("hasAuthority('system:menu:edit')")
    @PutMapping("/{id}")
    public AjaxResult<Void> edit(@PathVariable("id") Long id, @Valid @RequestBody MenuAddReq req) {
        menuService.editMenu(id, req);
        return AjaxResult.success();
    }

    @Operation(summary = "删除菜单")
    @PreAuthorize("hasAuthority('system:menu:delete')")
    @DeleteMapping("/{id}")
    public AjaxResult<Void> delete(@PathVariable("id") Long id) {
        menuService.deleteMenu(id);
        return AjaxResult.success();
    }
}
