package com.angu.matcher.admin.controller;

import com.angu.matcher.common.result.AjaxResult;
import com.angu.matcher.framework.security.JwtUtils;
import com.angu.matcher.framework.security.LoginUser;
import com.angu.matcher.framework.web.BaseController;
import com.angu.matcher.system.domain.SysUser;
import com.angu.matcher.system.dto.LoginRequest;
import com.angu.matcher.system.dto.LoginResponse;
import com.angu.matcher.system.dto.RefreshTokenRequest;
import com.angu.matcher.system.mapper.SysUserMapper;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "认证管理")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController extends BaseController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final SysUserMapper userMapper;

    @Operation(summary = "登录")
    @PostMapping("/login")
    public AjaxResult<LoginResponse> login(@Valid @RequestBody LoginRequest req) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));
            LoginUser lu = (LoginUser) auth.getPrincipal();
            String access = jwtUtils.generateAccessToken(lu.getUsername(), lu.getUserId());
            String refresh = jwtUtils.generateRefreshToken(lu.getUsername(), lu.getUserId());
            return AjaxResult.success(new LoginResponse(access, refresh, lu.getUserId(), lu.getUsername(), lu.getRealName()));
        } catch (BadCredentialsException e) {
            return AjaxResult.error(401, "用户名或密码错误");
        }
    }

    @Operation(summary = "刷新 Token")
    @PostMapping("/refresh")
    public AjaxResult<LoginResponse> refresh(@Valid @RequestBody RefreshTokenRequest req) {
        if (!jwtUtils.validateToken(req.getRefreshToken())) {
            return AjaxResult.error(401, "refreshToken 已失效，请重新登录");
        }
        Claims claims = jwtUtils.parseToken(req.getRefreshToken());
        if (!"refresh".equals(claims.get("type"))) {
            return AjaxResult.error(400, "Token 类型不正确");
        }
        String username = claims.getSubject();
        Long userId = claims.get("userId", Long.class);
        return AjaxResult.success(new LoginResponse(
                jwtUtils.generateAccessToken(username, userId),
                jwtUtils.generateRefreshToken(username, userId),
                userId, username, null));
    }

    @Operation(summary = "退出登录")
    @PostMapping("/logout")
    public AjaxResult<Void> logout() {
        return AjaxResult.success();
    }

    @Operation(summary = "获取当前用户信息")
    @GetMapping("/me")
    public AjaxResult<Object> me(@AuthenticationPrincipal LoginUser lu) {
        SysUser user = userMapper.selectById(lu.getUserId());
        return AjaxResult.success(user);
    }
}
