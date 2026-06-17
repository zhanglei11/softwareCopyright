package com.imaging.scheduler.admin.controller.auth;

import com.imaging.scheduler.common.core.AjaxResult;
import com.imaging.scheduler.framework.security.JwtUtil;
import com.imaging.scheduler.framework.security.UserDetailsServiceImpl;
import com.imaging.scheduler.framework.security.model.LoginUser;
import com.imaging.scheduler.system.dto.req.LoginReq;
import com.imaging.scheduler.system.dto.resp.LoginResp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "认证管理")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public AjaxResult<LoginResp> login(@Valid @RequestBody LoginReq req) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));
        LoginUser loginUser = (LoginUser) auth.getPrincipal();
        String accessToken = jwtUtil.generateAccessToken(loginUser.getUsername(), loginUser.getUserId());
        String refreshToken = jwtUtil.generateRefreshToken(loginUser.getUsername(), loginUser.getUserId());
        LoginResp resp = LoginResp.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(jwtUtil.getAccessExpireSeconds())
                .userInfo(LoginResp.UserInfo.builder()
                        .id(loginUser.getUserId())
                        .username(loginUser.getUsername())
                        .realName(loginUser.getRealName())
                        .roles(List.copyOf(loginUser.getPermissions()))
                        .build())
                .build();
        return AjaxResult.success("登录成功", resp);
    }

    @Operation(summary = "刷新Token")
    @PostMapping("/refresh")
    public AjaxResult<Map<String, Object>> refresh(@RequestBody Map<String, String> body) {
        String refreshToken = body.get("refreshToken");
        if (!jwtUtil.validateToken(refreshToken)) {
            return AjaxResult.error(401, "RefreshToken已过期，请重新登录");
        }
        String username = jwtUtil.getUsernameFromToken(refreshToken);
        Long userId = jwtUtil.getUserIdFromToken(refreshToken);
        String newAccessToken = jwtUtil.generateAccessToken(username, userId);
        return AjaxResult.success(Map.of("accessToken", newAccessToken, "expiresIn", jwtUtil.getAccessExpireSeconds()));
    }

    @Operation(summary = "退出登录")
    @PostMapping("/logout")
    public AjaxResult<Void> logout(@AuthenticationPrincipal LoginUser loginUser) {
        // Stateless JWT，客户端清除Token即可
        return AjaxResult.success();
    }
}
