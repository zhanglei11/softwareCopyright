package com.vqcc.admin.controller.system;

import com.vqcc.common.result.AjaxResult;
import com.vqcc.common.result.TableDataInfo;
import com.vqcc.framework.security.LoginUser;
import com.vqcc.system.domain.SysUser;
import com.vqcc.system.dto.request.UserCreateReq;
import com.vqcc.system.dto.request.UserUpdateReq;
import com.vqcc.system.service.ISysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@Tag(name = "用户管理")
@RestController
@RequestMapping("/api/v1/system/users")
@RequiredArgsConstructor
public class SysUserController {

    private final ISysUserService userService;

    @Operation(summary = "用户列表")
    @PreAuthorize("hasAuthority('system:user:list')")
    @GetMapping
    public AjaxResult<TableDataInfo<SysUser>> list(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String realName,
            @RequestParam(required = false) Integer status) {
        return AjaxResult.ok(TableDataInfo.ok(userService.list(username, realName, status)));
    }

    @Operation(summary = "用户详情")
    @PreAuthorize("hasAuthority('system:user:list')")
    @GetMapping("/{id}")
    public AjaxResult<SysUser> getById(@PathVariable Long id) {
        return AjaxResult.ok(userService.getById(id));
    }

    @Operation(summary = "创建用户")
    @PreAuthorize("hasAuthority('system:user:add')")
    @PostMapping
    public AjaxResult<Void> create(@Valid @RequestBody UserCreateReq req,
                                    @AuthenticationPrincipal LoginUser loginUser) {
        userService.create(req, loginUser.getUserId());
        return AjaxResult.ok(null);
    }

    @Operation(summary = "更新用户")
    @PreAuthorize("hasAuthority('system:user:edit')")
    @PutMapping("/{id}")
    public AjaxResult<Void> update(@PathVariable Long id, @RequestBody UserUpdateReq req,
                                    @AuthenticationPrincipal LoginUser loginUser) {
        req.setId(id);
        userService.update(req, loginUser.getUserId());
        return AjaxResult.ok(null);
    }

    @Operation(summary = "删除用户")
    @PreAuthorize("hasAuthority('system:user:delete')")
    @DeleteMapping("/{id}")
    public AjaxResult<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return AjaxResult.ok(null);
    }

    @Operation(summary = "重置密码")
    @PreAuthorize("hasAuthority('system:user:edit')")
    @PostMapping("/{id}/reset-password")
    public AjaxResult<Void> resetPassword(@PathVariable Long id) {
        userService.resetPassword(id);
        return AjaxResult.ok(null);
    }

    @Operation(summary = "修改状态")
    @PreAuthorize("hasAuthority('system:user:edit')")
    @PatchMapping("/{id}/status")
    public AjaxResult<Void> updateStatus(@PathVariable Long id, @RequestBody Map<String, Integer> body,
                                          @AuthenticationPrincipal LoginUser loginUser) {
        SysUser u = new SysUser();
        u.setId(id); u.setStatus(body.get("status")); u.setUpdatedBy(loginUser.getUserId());
        userService.update(new com.vqcc.system.dto.request.UserUpdateReq() {{
            setId(id); setStatus(body.get("status"));
        }}, loginUser.getUserId());
        return AjaxResult.ok(null);
    }
}
