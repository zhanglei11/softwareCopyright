package com.sursoft.iidp.system.auth.service;

import com.sursoft.iidp.system.auth.dto.LoginRequest;
import com.sursoft.iidp.system.auth.dto.LoginResponse;
import com.sursoft.iidp.system.auth.dto.RefreshTokenRequest;

public interface AuthService {
    LoginResponse login(LoginRequest request);
    LoginResponse refreshToken(RefreshTokenRequest request);
    void logout(String username);
}
