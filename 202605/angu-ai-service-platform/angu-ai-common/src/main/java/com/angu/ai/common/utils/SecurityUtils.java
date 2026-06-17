package com.angu.ai.common.utils;

import com.angu.ai.common.exception.ServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    private SecurityUtils() {}

    public static Long getUserId() {
        return getLoginUser().getUserId();
    }

    public static com.angu.ai.common.core.domain.LoginUser getLoginUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new ServiceException(401, "未登录或登录已过期");
        }
        Object principal = auth.getPrincipal();
        if (principal instanceof com.angu.ai.common.core.domain.LoginUser loginUser) {
            return loginUser;
        }
        throw new ServiceException(401, "无法获取当前用户信息");
    }

    public static boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return false;
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_super_admin") 
                            || a.getAuthority().equals("ROLE_ai_admin"));
    }
}
