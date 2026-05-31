package com.sursoft.iidp.system.auth.service;

import com.sursoft.iidp.common.exception.BusinessException;
import com.sursoft.iidp.framework.security.JwtTokenUtil;
import com.sursoft.iidp.framework.security.LoginUser;
import com.sursoft.iidp.system.auth.dto.LoginRequest;
import com.sursoft.iidp.system.auth.dto.LoginResponse;
import com.sursoft.iidp.system.auth.dto.RefreshTokenRequest;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public LoginResponse login(LoginRequest request) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            LoginUser loginUser = (LoginUser) auth.getPrincipal();
            LoginResponse response = new LoginResponse();
            response.setAccessToken(jwtTokenUtil.generateAccessToken(loginUser.getUsername(), loginUser.getUserId()));
            response.setRefreshToken(jwtTokenUtil.generateRefreshToken(loginUser.getUsername(), loginUser.getUserId()));
            return response;
        } catch (BadCredentialsException e) {
            throw new BusinessException(401, "用户名或密码错误");
        }
    }

    @Override
    public LoginResponse refreshToken(RefreshTokenRequest request) {
        if (!jwtTokenUtil.validateToken(request.getRefreshToken())) {
            throw new BusinessException(401, "refreshToken已失效，请重新登录");
        }
        Claims claims = jwtTokenUtil.parseToken(request.getRefreshToken());
        if (!"refresh".equals(claims.get("type"))) {
            throw new BusinessException(400, "Token类型不正确");
        }
        String username = claims.getSubject();
        Long userId = claims.get("userId", Long.class);
        LoginResponse response = new LoginResponse();
        response.setAccessToken(jwtTokenUtil.generateAccessToken(username, userId));
        response.setRefreshToken(jwtTokenUtil.generateRefreshToken(username, userId));
        return response;
    }

    @Override
    public void logout(String username) {
        // stateless JWT, 客户端删除Token即可；如需黑名单可扩展Redis
    }
}
