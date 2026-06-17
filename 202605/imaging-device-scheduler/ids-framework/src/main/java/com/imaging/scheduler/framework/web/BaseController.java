package com.imaging.scheduler.framework.web;

import com.imaging.scheduler.framework.security.model.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class BaseController {

    protected LoginUser getLoginUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof LoginUser lu) return lu;
        return null;
    }

    protected Long getUserId() {
        LoginUser lu = getLoginUser();
        return lu != null ? lu.getUserId() : null;
    }

    protected String getUsername() {
        LoginUser lu = getLoginUser();
        return lu != null ? lu.getUsername() : null;
    }
}
