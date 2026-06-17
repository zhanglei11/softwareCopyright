package com.sursoft.iidp.system.sys.controller;

import com.sursoft.iidp.common.core.AjaxResult;
import com.sursoft.iidp.common.core.PageDomain;
import com.sursoft.iidp.common.core.TableDataInfo;
import com.sursoft.iidp.framework.web.BaseController;
import com.sursoft.iidp.system.sys.domain.SysUser;
import com.sursoft.iidp.system.sys.service.SysUserService;
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
    private final SysUserService userService;

    @Operation(summary = "用户列表")
    @PreAuthorize("hasAuthority('system:user:list')")
    @GetMapping
    public TableDataInfo<SysUser> list(SysUser query, PageDomain page) {
        startPage(page);
        return getDataTable(userService.listUsers(query));
    }

    @Operation(summary = "用户详情")
    @PreAuthorize("hasAuthority('system:user:list')")
    @GetMapping("/{id}")
    public AjaxResult<SysUser> getInfo(@PathVariable Long id) {
        return AjaxResult.success(userService.getUserById(id));
    }

    @Operation(summary = "新增用户")
    @PreAuthorize("hasAuthority('system:user:add')")
    @PostMapping
    public AjaxResult<Void> add(@Valid @RequestBody SysUser user) {
        userService.addUser(user);
        return AjaxResult.success();
    }

    @Operation(summary = "修改用户")
    @PreAuthorize("hasAuthority('system:user:edit')")
    @PutMapping("/{id}")
    public AjaxResult<Void> edit(@PathVariable Long id, @RequestBody SysUser user) {
        user.setId(id);
        userService.editUser(user);
        return AjaxResult.success();
    }

    @Operation(summary = "修改用户状态")
    @PreAuthorize("hasAuthority('system:user:edit')")
    @PatchMapping("/{id}/status")
    public AjaxResult<Void> updateStatus(@PathVariable Long id, @RequestBody Map<String, Integer> body) {
        userService.updateStatus(id, body.get("status"));
        return AjaxResult.success();
    }

    @Operation(summary = "重置密码")
    @PreAuthorize("hasAuthority('system:user:edit')")
    @PutMapping("/{id}/reset-password")
    public AjaxResult<Void> resetPwd(@PathVariable Long id, @RequestBody Map<String, String> body) {
        userService.resetPassword(id, body.get("password"));
        return AjaxResult.success();
    }

    @Operation(summary = "删除用户")
    @PreAuthorize("hasAuthority('system:user:delete')")
    @DeleteMapping("/{id}")
    public AjaxResult<Void> remove(@PathVariable Long id) {
        userService.removeUser(id);
        return AjaxResult.success();
    }
}
