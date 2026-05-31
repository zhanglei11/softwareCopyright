package com.sursoft.sfd.admin.controller.auth;

import com.sursoft.sfd.common.core.AjaxResult;
import com.sursoft.sfd.framework.security.JwtUtils;
import com.sursoft.sfd.common.security.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "认证管理")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Operation(summary = "登录")
    @PostMapping("/login")
    public AjaxResult<Object> login(@Valid @RequestBody LoginRequest req) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));
        LoginUser loginUser = (LoginUser) auth.getPrincipal();
        String token = jwtUtils.generateToken(loginUser.getUserId(), loginUser.getUsername());
        return AjaxResult.ok(java.util.Map.of(
                "accessToken", token,
                "tokenType", "Bearer",
                "expiresIn", 7200
        ));
    }

    @Operation(summary = "退出登录")
    @PostMapping("/logout")
    public AjaxResult<Void> logout() {
        return AjaxResult.ok();
    }

    @Data
    public static class LoginRequest {
        @NotBlank(message = "账号不能为空") private String username;
        @NotBlank(message = "密码不能为空") private String password;
    }
}
