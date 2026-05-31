package com.angu.ai.admin.controller;

import com.angu.ai.common.core.domain.AjaxResult;
import com.angu.ai.common.core.domain.LoginUser;
import com.angu.ai.common.utils.SecurityUtils;
import com.angu.ai.framework.security.JwtTokenProvider;
import com.angu.ai.framework.security.service.UserDetailsServiceImpl;
import com.angu.ai.system.domain.dto.LoginDTO;
import com.angu.ai.system.domain.vo.LoginVO;
import com.angu.ai.system.domain.vo.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
    private final AuthenticationManager authManager;
    private final JwtTokenProvider tokenProvider;
    private final UserDetailsServiceImpl userDetailsService;

    @Operation(summary = "登录")
    @PostMapping("/login")
    public AjaxResult<LoginVO> login(@Valid @RequestBody LoginDTO dto) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));
        LoginUser loginUser = (LoginUser) auth.getPrincipal();
        String token = tokenProvider.generate(loginUser);
        UserVO userInfo = new UserVO();
        userInfo.setId(loginUser.getUserId());
        userInfo.setUsername(loginUser.getUsername());
        userInfo.setRealName(loginUser.getRealName());
        LoginVO vo = new LoginVO();
        vo.setAccessToken(token);
        vo.setTokenType("Bearer");
        vo.setPermissions(loginUser.getPermissions());
        vo.setRoles(loginUser.getRoles());
        vo.setUserInfo(userInfo);
        return AjaxResult.success(vo);
    }

    @Operation(summary = "获取当前用户信息")
    @GetMapping("/me")
    public AjaxResult<LoginVO> me() {
        LoginUser user = SecurityUtils.getLoginUser();
        UserVO userInfo = new UserVO();
        userInfo.setId(user.getUserId());
        userInfo.setUsername(user.getUsername());
        userInfo.setRealName(user.getRealName());
        LoginVO vo = new LoginVO();
        vo.setPermissions(user.getPermissions());
        vo.setRoles(user.getRoles());
        vo.setUserInfo(userInfo);
        return AjaxResult.success(vo);
    }

    @Operation(summary = "登出")
    @PostMapping("/logout")
    public AjaxResult<Void> logout() { return AjaxResult.success(); }
}
