package com.vqcc.admin.controller;

import com.vqcc.common.result.AjaxResult;
import com.vqcc.framework.security.JwtUtils;
import com.vqcc.framework.security.LoginUser;
import com.vqcc.system.domain.SysUser;
import com.vqcc.system.dto.request.LoginReq;
import com.vqcc.system.mapper.SysUserMapper;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "认证管理")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtUtils jwtUtils;
    private final SysUserMapper userMapper;

    @Operation(summary = "登录")
    @PostMapping("/login")
    public AjaxResult<Map<String, Object>> login(@Valid @RequestBody LoginReq req) {
        Authentication auth;
        try {
            auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));
        } catch (BadCredentialsException e) {
            return AjaxResult.fail(401, "用户名或密码错误");
        } catch (Exception e) {
            return AjaxResult.fail(401, "认证失败：" + e.getMessage());
        }
        LoginUser loginUser = (LoginUser) auth.getPrincipal();
        SysUser user = loginUser.getUser();
        String accessToken = jwtUtils.generateToken(user.getId(), user.getUsername(), "access");
        String refreshToken = jwtUtils.generateToken(user.getId(), user.getUsername(), "refresh");
        List<String> roles = userMapper.selectRoleCodesByUserId(user.getId());
        Map<String, Object> data = new HashMap<>();
        data.put("accessToken", accessToken);
        data.put("refreshToken", refreshToken);
        data.put("userId", user.getId());
        data.put("username", user.getUsername());
        data.put("realName", user.getRealName());
        data.put("roles", roles);
        data.put("permissions", loginUser.getPermissions());
        return AjaxResult.ok(data);
    }

    @Operation(summary = "获取当前用户信息")
    @GetMapping("/userinfo")
    public AjaxResult<Map<String, Object>> userinfo(@AuthenticationPrincipal LoginUser loginUser) {
        SysUser user = loginUser.getUser();
        List<String> roles = userMapper.selectRoleCodesByUserId(user.getId());
        Map<String, Object> data = new HashMap<>();
        data.put("userId", user.getId());
        data.put("username", user.getUsername());
        data.put("realName", user.getRealName());
        data.put("phone", user.getPhone());
        data.put("dept", user.getDept());
        data.put("roles", roles);
        data.put("permissions", loginUser.getPermissions());
        return AjaxResult.ok(data);
    }

    @Operation(summary = "退出登录")
    @PostMapping("/logout")
    public AjaxResult<Void> logout() {
        return AjaxResult.ok(null);
    }
}
