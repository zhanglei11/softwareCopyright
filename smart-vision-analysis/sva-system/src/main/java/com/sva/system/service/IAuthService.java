package com.sva.system.service;

import com.sva.system.vo.LoginVO;

public interface IAuthService {
    LoginVO login(String username, String password);
    LoginVO refreshToken(String refreshToken);
    void logout(Long userId);
}
