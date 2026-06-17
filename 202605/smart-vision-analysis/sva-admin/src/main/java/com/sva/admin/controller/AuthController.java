package com.sva.admin.controller;

import com.sva.common.core.domain.AjaxResult;
import com.sva.framework.security.LoginUser;
import com.sva.system.service.IAuthService;
import com.sva.system.vo.LoginVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "认证管理", description = "登录/刷新/退出")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IAuthService authService;

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public AjaxResult<LoginVO> login(@RequestBody LoginRequest req) {
        return AjaxResult.success(authService.login(req.getUsername(), req.getPassword()));
    }

    @Operation(summary = "刷新Token")
    @PostMapping("/token/refresh")
    public AjaxResult<LoginVO> refresh(@RequestBody RefreshRequest req) {
        return AjaxResult.success(authService.refreshToken(req.getRefreshToken()));
    }

    @Operation(summary = "退出登录")
    @PostMapping("/logout")
    public AjaxResult<Void> logout(@AuthenticationPrincipal LoginUser user) {
        if (user != null) authService.logout(user.getUserId());
        return AjaxResult.success();
    }

    @Operation(summary = "获取当前用户信息")
    @GetMapping("/userInfo")
    public AjaxResult<Object> userInfo(@AuthenticationPrincipal LoginUser user) {
        if (user == null) return AjaxResult.error(401, "未登录");
        java.util.Map<String, Object> info = new java.util.LinkedHashMap<>();
        info.put("id", user.getUserId());
        info.put("username", user.getUsername());
        info.put("realName", user.getRealName());
        info.put("roles", user.getRoles());
        info.put("permissions", user.getPermissions());
        return AjaxResult.success(info);
    }

    @Data
    public static class LoginRequest {
        private String username;
        private String password;
    }

    @Data
    public static class RefreshRequest {
        private String refreshToken;
    }
}
