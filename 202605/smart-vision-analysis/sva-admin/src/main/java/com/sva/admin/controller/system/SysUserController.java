package com.sva.admin.controller.system;

import com.sva.common.core.controller.BaseController;
import com.sva.common.core.domain.AjaxResult;
import com.sva.common.core.domain.TableDataInfo;
import com.sva.framework.security.LoginUser;
import com.sva.system.domain.SysUser;
import com.sva.system.query.UserQuery;
import com.sva.system.service.ISysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "用户管理")
@RestController
@RequestMapping("/api/system/users")
@RequiredArgsConstructor
public class SysUserController extends BaseController {

    private final ISysUserService userService;

    @Operation(summary = "用户列表")
    @PreAuthorize("hasAuthority('system:user:list')")
    @GetMapping
    public TableDataInfo list(UserQuery query,
                              @RequestParam(defaultValue = "1") int pageNum,
                              @RequestParam(defaultValue = "10") int pageSize) {
        startPage(pageNum, pageSize);
        return getDataTable(userService.list(query));
    }

    @Operation(summary = "用户详情")
    @GetMapping("/{id}")
    public AjaxResult<SysUser> getInfo(@PathVariable Long id) {
        return AjaxResult.success(userService.getById(id));
    }

    @Operation(summary = "新增用户")
    @PreAuthorize("hasAuthority('system:user:add')")
    @PostMapping
    public AjaxResult<Void> add(@RequestBody SysUser user, @AuthenticationPrincipal LoginUser loginUser) {
        userService.add(user, loginUser.getUserId());
        return AjaxResult.success();
    }

    @Operation(summary = "修改用户")
    @PreAuthorize("hasAuthority('system:user:edit')")
    @PutMapping("/{id}")
    public AjaxResult<Void> edit(@PathVariable Long id, @RequestBody SysUser user,
                                 @AuthenticationPrincipal LoginUser loginUser) {
        user.setId(id);
        userService.update(user, loginUser.getUserId());
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
    @PostMapping("/{id}/password/reset")
    public AjaxResult<Void> resetPassword(@PathVariable Long id) {
        userService.resetPassword(id);
        return AjaxResult.success();
    }

    @Operation(summary = "删除用户")
    @PreAuthorize("hasAuthority('system:user:delete')")
    @DeleteMapping("/{id}")
    public AjaxResult<Void> delete(@PathVariable Long id) {
        userService.deleteById(id);
        return AjaxResult.success();
    }

    @Operation(summary = "分配角色")
    @PreAuthorize("hasAuthority('system:user:edit')")
    @PostMapping("/{id}/roles")
    public AjaxResult<Void> assignRoles(@PathVariable Long id, @RequestBody Map<String, List<Long>> body) {
        userService.assignRoles(id, body.get("roleIds"));
        return AjaxResult.success();
    }
}
