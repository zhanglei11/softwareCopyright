package com.sursoft.sfd.admin.controller.system;

import com.github.pagehelper.PageInfo;
import com.sursoft.sfd.common.core.AjaxResult;
import com.sursoft.sfd.common.core.PageResult;
import com.sursoft.sfd.framework.web.BaseController;
import com.sursoft.sfd.system.domain.SysUser;
import com.sursoft.sfd.system.service.ISysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "用户管理")
@RestController
@RequestMapping("/api/system/users")
@RequiredArgsConstructor
public class SysUserController extends BaseController {
    private final ISysUserService userService;

    @Operation(summary = "用户列表")
    @GetMapping
    public AjaxResult<PageResult<SysUser>> list(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String realName,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return getDataTable(pageNum, pageSize, () -> userService.list(username, realName, status));
    }

    @Operation(summary = "用户详情")
    @GetMapping("/{id}")
    public AjaxResult<SysUser> getById(@PathVariable Long id) {
        return AjaxResult.ok(userService.getById(id));
    }

    @Operation(summary = "新增用户")
    @PostMapping
    public AjaxResult<Void> add(@RequestBody UserDTO dto) {
        userService.add(dto.toUser(), dto.getRoleIds(), getCurrentUserId());
        return AjaxResult.ok();
    }

    @Operation(summary = "编辑用户")
    @PutMapping("/{id}")
    public AjaxResult<Void> edit(@PathVariable Long id, @RequestBody UserDTO dto) {
        userService.edit(id, dto.toUser(), getCurrentUserId());
        return AjaxResult.ok();
    }

    @Operation(summary = "启用/禁用用户")
    @PutMapping("/{id}/status")
    public AjaxResult<Void> status(@PathVariable Long id, @RequestParam Integer status) {
        userService.updateStatus(id, status, getCurrentUserId());
        return AjaxResult.ok();
    }

    @Operation(summary = "重置密码")
    @PutMapping("/{id}/reset-password")
    public AjaxResult<Void> resetPassword(@PathVariable Long id) {
        userService.resetPassword(id, getCurrentUserId());
        return AjaxResult.ok();
    }

    @Operation(summary = "删除用户")
    @DeleteMapping("/{id}")
    public AjaxResult<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return AjaxResult.ok();
    }

    @Data
    public static class UserDTO {
        private String username;
        private String realName;
        private String password;
        private String phone;
        private String department;
        private Integer status;
        private List<Long> roleIds;
        public SysUser toUser() {
            SysUser u = new SysUser();
            u.setUsername(username); u.setRealName(realName);
            u.setPassword(password); u.setPhone(phone);
            u.setDepartment(department); u.setStatus(status);
            return u;
        }
    }
}
