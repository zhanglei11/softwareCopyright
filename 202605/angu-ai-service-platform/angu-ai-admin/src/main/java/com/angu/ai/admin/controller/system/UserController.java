package com.angu.ai.admin.controller.system;

import com.angu.ai.common.core.domain.AjaxResult;
import com.angu.ai.common.core.page.TableDataInfo;
import com.angu.ai.common.utils.SecurityUtils;
import com.angu.ai.system.domain.dto.UserCreateDTO;
import com.angu.ai.system.domain.query.UserQuery;
import com.angu.ai.system.domain.vo.UserVO;
import com.angu.ai.system.service.ISysUserService;
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
public class UserController {
    private final ISysUserService userService;

    @Operation(summary = "用户分页列表")
    @GetMapping
    @PreAuthorize("hasAuthority('system:user:list')")
    public AjaxResult<TableDataInfo<UserVO>> page(UserQuery query) {
        return AjaxResult.success(userService.pageList(query));
    }

    @Operation(summary = "用户详情")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('system:user:query')")
    public AjaxResult<UserVO> get(@PathVariable Long id) {
        return AjaxResult.success(userService.getById(id));
    }

    @Operation(summary = "新增用户")
    @PostMapping
    @PreAuthorize("hasAuthority('system:user:add')")
    public AjaxResult<Void> create(@Valid @RequestBody UserCreateDTO dto) {
        userService.create(dto); return AjaxResult.success();
    }

    @Operation(summary = "编辑用户")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('system:user:edit')")
    public AjaxResult<Void> update(@PathVariable Long id, @Valid @RequestBody UserCreateDTO dto) {
        userService.update(id, dto); return AjaxResult.success();
    }

    @Operation(summary = "删除用户")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('system:user:delete')")
    public AjaxResult<Void> delete(@PathVariable Long id) {
        userService.deleteById(id); return AjaxResult.success();
    }

    @Operation(summary = "重置密码")
    @PutMapping("/{id}/password/reset")
    @PreAuthorize("hasAuthority('system:user:edit')")
    public AjaxResult<Void> resetPassword(@PathVariable Long id) {
        userService.resetPassword(id); return AjaxResult.success();
    }

    @Operation(summary = "启用/禁用用户")
    @PutMapping("/{id}/status/{status}")
    @PreAuthorize("hasAuthority('system:user:edit')")
    public AjaxResult<Void> updateStatus(@PathVariable Long id, @PathVariable Integer status) {
        userService.updateStatus(id, status); return AjaxResult.success();
    }

    @Operation(summary = "用户使用量")
    @GetMapping("/{id}/usage")
    @PreAuthorize("hasAuthority('system:user:query')")
    public AjaxResult<Map<String, Object>> usage(@PathVariable Long id) {
        return AjaxResult.success(userService.getUserUsage(id));
    }
}
