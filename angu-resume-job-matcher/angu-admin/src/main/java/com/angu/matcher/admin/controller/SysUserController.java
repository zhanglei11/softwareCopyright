package com.angu.matcher.admin.controller;

import com.angu.matcher.common.result.AjaxResult;
import com.angu.matcher.common.result.TableDataInfo;
import com.angu.matcher.framework.web.BaseController;
import com.angu.matcher.system.dto.*;
import com.angu.matcher.system.service.ISysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "用户管理")
@RestController
@RequestMapping("/api/system/users")
@RequiredArgsConstructor
public class SysUserController extends BaseController {

    private final ISysUserService userService;

    @Operation(summary = "用户分页列表")
    @PreAuthorize("hasAuthority('system:user:list')")
    @GetMapping
    public AjaxResult<TableDataInfo<?>> list(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        startPage(page, size);
        return AjaxResult.success(getDataTable(userService.listUsers(username, phone, status)));
    }

    @Operation(summary = "用户详情")
    @PreAuthorize("hasAuthority('system:user:list')")
    @GetMapping("/{id}")
    public AjaxResult<?> getById(@PathVariable Long id) {
        return AjaxResult.success(userService.getById(id));
    }

    @Operation(summary = "新增用户")
    @PreAuthorize("hasAuthority('system:user:add')")
    @PostMapping
    public AjaxResult<Void> create(@Valid @RequestBody UserCreateRequest req) {
        userService.createUser(req);
        return AjaxResult.success();
    }

    @Operation(summary = "编辑用户")
    @PreAuthorize("hasAuthority('system:user:edit')")
    @PutMapping("/{id}")
    public AjaxResult<Void> update(@PathVariable Long id, @RequestBody UserUpdateRequest req) {
        userService.updateUser(id, req);
        return AjaxResult.success();
    }

    @Operation(summary = "删除用户")
    @PreAuthorize("hasAuthority('system:user:delete')")
    @DeleteMapping("/{id}")
    public AjaxResult<Void> delete(@PathVariable Long id) {
        userService.deleteUser(id, getUserId());
        return AjaxResult.success();
    }

    @Operation(summary = "重置密码")
    @PreAuthorize("hasAuthority('system:user:edit')")
    @PutMapping("/{id}/reset-password")
    public AjaxResult<Void> resetPassword(@PathVariable Long id, @Valid @RequestBody ResetPasswordRequest req) {
        userService.resetPassword(id, req.getNewPassword());
        return AjaxResult.success();
    }

    @Operation(summary = "启用/禁用")
    @PreAuthorize("hasAuthority('system:user:edit')")
    @PutMapping("/{id}/status")
    public AjaxResult<Void> updateStatus(@PathVariable Long id, @RequestBody Map<String, Integer> body) {
        userService.updateStatus(id, body.get("status"));
        return AjaxResult.success();
    }
}
