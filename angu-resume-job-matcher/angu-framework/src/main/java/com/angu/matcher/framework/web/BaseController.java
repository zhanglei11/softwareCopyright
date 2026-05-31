package com.angu.matcher.framework.web;

import com.angu.matcher.common.result.TableDataInfo;
import com.angu.matcher.common.utils.PageUtils;
import com.angu.matcher.framework.security.LoginUser;
import com.github.pagehelper.PageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

public class BaseController {

    protected void startPage(int page, int size) {
        PageHelper.startPage(page, size);
    }

    protected <T> TableDataInfo<T> getDataTable(List<T> list) {
        return PageUtils.build(list);
    }

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

    protected String getRealName() {
        LoginUser lu = getLoginUser();
        return lu != null ? lu.getRealName() : null;
    }
}
