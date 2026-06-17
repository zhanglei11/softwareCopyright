package com.imaging.scheduler.admin.controller.system;

import com.imaging.scheduler.common.core.AjaxResult;
import com.imaging.scheduler.common.core.TableDataInfo;
import com.imaging.scheduler.framework.web.BaseController;
import com.imaging.scheduler.system.domain.system.SysUser;
import com.imaging.scheduler.system.dto.req.UserAddReq;
import com.imaging.scheduler.system.dto.req.UserEditReq;
import com.imaging.scheduler.system.dto.req.UserQueryReq;
import com.imaging.scheduler.system.service.system.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "用户管理")
@RestController
@RequestMapping("/api/v1/system/users")
@RequiredArgsConstructor
public class SysUserController extends BaseController {

    private final SysUserService userService;

    @Operation(summary = "用户分页列表")
    @PreAuthorize("hasAuthority('system:user:list')")
    @GetMapping
    public TableDataInfo<SysUser> list(UserQueryReq req) {
        return userService.getUserList(req);
    }

    @Operation(summary = "用户详情")
    @PreAuthorize("hasAuthority('system:user:list')")
    @GetMapping("/{id}")
    public AjaxResult<SysUser> detail(@PathVariable("id") Long id) {
        return AjaxResult.success(userService.getUserById(id));
    }

    @Operation(summary = "新增用户")
    @PreAuthorize("hasAuthority('system:user:add')")
    @PostMapping
    public AjaxResult<Void> add(@Valid @RequestBody UserAddReq req) {
        userService.addUser(req);
        return AjaxResult.success();
    }

    @Operation(summary = "编辑用户")
    @PreAuthorize("hasAuthority('system:user:edit')")
    @PutMapping("/{id}")
    public AjaxResult<Void> edit(@PathVariable("id") Long id, @Valid @RequestBody UserEditReq req) {
        userService.editUser(id, req);
        return AjaxResult.success();
    }

    @Operation(summary = "切换用户状态")
    @PreAuthorize("hasAuthority('system:user:edit')")
    @PatchMapping("/{id}/status")
    public AjaxResult<Void> updateStatus(@PathVariable("id") Long id, @RequestBody Map<String, Integer> body) {
        userService.updateStatus(id, body.get("status"));
        return AjaxResult.success();
    }

    @Operation(summary = "重置密码")
    @PreAuthorize("hasAuthority('system:user:edit')")
    @PostMapping("/{id}/reset-password")
    public AjaxResult<Void> resetPassword(@PathVariable("id") Long id) {
        userService.resetPassword(id);
        return AjaxResult.success();
    }

    @Operation(summary = "删除用户")
    @PreAuthorize("hasAuthority('system:user:delete')")
    @DeleteMapping("/{id}")
    public AjaxResult<Void> delete(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return AjaxResult.success();
    }
}
