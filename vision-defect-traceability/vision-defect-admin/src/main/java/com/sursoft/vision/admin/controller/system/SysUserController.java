package com.sursoft.vision.admin.controller.system;

import com.sursoft.vision.common.core.AjaxResult;
import com.sursoft.vision.common.core.BaseController;
import com.sursoft.vision.common.core.TableDataInfo;
import com.sursoft.vision.system.domain.SysUser;
import com.sursoft.vision.system.dto.UserAddDTO;
import com.sursoft.vision.system.dto.UserEditDTO;
import com.sursoft.vision.system.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/system/users")
@Tag(name = "用户管理")
@RequiredArgsConstructor
public class SysUserController extends BaseController {

    private final SysUserService userService;

    @GetMapping
    @Operation(summary = "查询用户列表")
    @PreAuthorize("hasAuthority('system:user:list')")
    public TableDataInfo<SysUser> list(@RequestParam(value = "username", required=false) String username,
            @RequestParam(value = "realName", required=false) String realName,
            @RequestParam(value = "status", required=false) Integer status,
            @RequestParam(value = "pageNum", defaultValue="1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue="10") int pageSize) {
        return userService.list(username, realName, status, pageNum, pageSize);
    }

    @PostMapping
    @Operation(summary = "新增用户")
    @PreAuthorize("hasAuthority('system:user:add')")
    public AjaxResult<Void> add(@Valid @RequestBody UserAddDTO dto) {
        userService.add(dto);
        return AjaxResult.success();
    }

    @PutMapping("/{id}")
    @Operation(summary = "编辑用户")
    @PreAuthorize("hasAuthority('system:user:edit')")
    public AjaxResult<Void> edit(@PathVariable Long id, @Valid @RequestBody UserEditDTO dto) {
        userService.edit(id, dto);
        return AjaxResult.success();
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "启用/禁用用户")
    @PreAuthorize("hasAuthority('system:user:edit')")
    public AjaxResult<Void> updateStatus(@PathVariable Long id, @RequestBody Map<String, Integer> body) {
        userService.updateStatus(id, body.get("status"));
        return AjaxResult.success();
    }

    @PostMapping("/{id}/reset-password")
    @Operation(summary = "重置密码")
    @PreAuthorize("hasAuthority('system:user:edit')")
    public AjaxResult<Void> resetPassword(@PathVariable Long id) {
        userService.resetPassword(id);
        return AjaxResult.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户")
    @PreAuthorize("hasAuthority('system:user:delete')")
    public AjaxResult<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return AjaxResult.success();
    }
}
