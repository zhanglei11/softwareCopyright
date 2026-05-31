package com.sursoft.iidp.system.auth.controller;

import com.sursoft.iidp.common.core.AjaxResult;
import com.sursoft.iidp.framework.security.LoginUser;
import com.sursoft.iidp.system.auth.dto.LoginRequest;
import com.sursoft.iidp.system.auth.dto.LoginResponse;
import com.sursoft.iidp.system.auth.dto.RefreshTokenRequest;
import com.sursoft.iidp.system.auth.service.AuthService;
import com.sursoft.iidp.system.sys.domain.SysMenu;
import com.sursoft.iidp.system.sys.service.SysMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "认证管理", description = "/api/auth")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final SysMenuService menuService;

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public AjaxResult<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return AjaxResult.success(authService.login(request));
    }

    @Operation(summary = "刷新Token")
    @PostMapping("/refresh")
    public AjaxResult<LoginResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        return AjaxResult.success(authService.refreshToken(request));
    }

    @Operation(summary = "退出登录")
    @PostMapping("/logout")
    public AjaxResult<Void> logout(@AuthenticationPrincipal LoginUser loginUser) {
        if (loginUser != null) {
            authService.logout(loginUser.getUsername());
        }
        return AjaxResult.success();
    }

    @Operation(summary = "获取当前用户菜单树")
    @GetMapping("/menus")
    public AjaxResult<List<SysMenu>> getMenus(@AuthenticationPrincipal LoginUser loginUser) {
        return AjaxResult.success(menuService.getMenuTreeByUserId(loginUser.getUserId()));
    }
}
