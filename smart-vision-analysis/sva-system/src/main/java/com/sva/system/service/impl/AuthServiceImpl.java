package com.sva.system.service.impl;

import com.sva.common.exception.ServiceException;
import com.sva.framework.security.JwtTokenUtil;
import com.sva.framework.security.LoginUser;
import com.sva.system.domain.SysUser;
import com.sva.system.mapper.SysUserMapper;
import com.sva.system.query.UserQuery;
import com.sva.system.service.IAuthService;
import com.sva.system.vo.LoginVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final SysUserMapper userMapper;

    @Override
    public LoginVO login(String username, String password) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));
            SecurityContextHolder.getContext().setAuthentication(auth);
            LoginUser loginUser = (LoginUser) auth.getPrincipal();
            String accessToken = jwtTokenUtil.generateAccessToken(loginUser);
            String refreshToken = jwtTokenUtil.generateRefreshToken(loginUser);
            LoginVO vo = new LoginVO();
            vo.setAccessToken(accessToken);
            vo.setRefreshToken(refreshToken);
            vo.setExpiresIn(7200L);
            LoginVO.UserInfoVO info = new LoginVO.UserInfoVO();
            info.setId(loginUser.getUserId());
            info.setUsername(loginUser.getUsername());
            info.setRealName(loginUser.getRealName());
            info.setRoles(loginUser.getRoles());
            vo.setUserInfo(info);
            return vo;
        } catch (AuthenticationException e) {
            throw new ServiceException(401, "用户名或密码错误");
        }
    }

    @Override
    public LoginVO refreshToken(String refreshToken) {
        var claims = jwtTokenUtil.parseToken(refreshToken);
        if (claims == null || !"refresh".equals(claims.get("type"))) {
            throw new ServiceException(401, "无效的刷新令牌");
        }
        String username = claims.getSubject();
        UserQuery q = new UserQuery();
        q.setUsername(username);
        List<SysUser> users = userMapper.selectList(q);
        if (users.isEmpty()) throw new ServiceException(401, "用户不存在");
        SysUser user = users.get(0);
        // 构造临时 LoginUser
        LoginUser lu = new LoginUser();
        lu.setUserId(user.getId());
        lu.setUsername(user.getUsername());
        lu.setPassword(user.getPassword());
        lu.setRealName(user.getRealName());
        lu.setStatus(user.getStatus());
        String newAccess = jwtTokenUtil.generateAccessToken(lu);
        LoginVO vo = new LoginVO();
        vo.setAccessToken(newAccess);
        vo.setRefreshToken(refreshToken);
        vo.setExpiresIn(7200L);
        return vo;
    }

    @Override
    public void logout(Long userId) {
        SecurityContextHolder.clearContext();
    }
}
