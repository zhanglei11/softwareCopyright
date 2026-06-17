package com.sursoft.vision.admin.controller.system;

import com.sursoft.vision.common.core.AjaxResult;
import com.sursoft.vision.framework.security.JwtTokenProvider;
import com.sursoft.vision.framework.security.LoginUser;
import com.sursoft.vision.system.dto.LoginDTO;
import com.sursoft.vision.system.vo.LoginVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "认证管理", description = "登录/登出/刷新Token")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "账号密码登录，返回 JWT Token")
    public AjaxResult<LoginVO> login(@Valid @RequestBody LoginDTO dto) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));
        LoginUser loginUser = (LoginUser) auth.getPrincipal();
        String token = tokenProvider.generateToken(loginUser.getUsername(), loginUser.getUser().getId());
        LoginVO vo = LoginVO.builder()
                .accessToken(token)
                .expiresIn(tokenProvider.getExpirationSeconds())
                .userInfo(LoginVO.UserInfoVO.builder()
                        .userId(loginUser.getUser().getId())
                        .realName(loginUser.getUser().getRealName())
                        .roles(loginUser.getRoles())
                        .perms(loginUser.getPerms())
                        .build())
                .build();
        return AjaxResult.success(vo);
    }

    @PostMapping("/logout")
    @Operation(summary = "退出登录")
    public AjaxResult<Void> logout() {
        return AjaxResult.success();
    }
}
