package com.sursoft.sfd.framework.web;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sursoft.sfd.common.core.AjaxResult;
import com.sursoft.sfd.common.core.PageResult;
import com.sursoft.sfd.common.security.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.function.Supplier;

public class BaseController {

    protected void startPage(int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
    }

    protected <T> AjaxResult<PageResult<T>> getDataTable(PageInfo<T> pageInfo) {
        return AjaxResult.ok(PageResult.of(pageInfo.getTotal(),
                pageInfo.getPageNum(), pageInfo.getPageSize(), pageInfo.getList()));
    }

    protected <T> AjaxResult<PageResult<T>> getDataTable(int page, int pageSize,
                                                          Supplier<List<T>> query) {
        startPage(page, pageSize);
        List<T> list = query.get();
        PageInfo<T> pageInfo = new PageInfo<>(list);
        return AjaxResult.ok(PageResult.of(pageInfo.getTotal(), page, pageSize, list));
    }

    protected LoginUser getLoginUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof LoginUser) {
            return (LoginUser) auth.getPrincipal();
        }
        return null;
    }

    protected Long getCurrentUserId() {
        LoginUser loginUser = getLoginUser();
        return loginUser != null ? loginUser.getUserId() : null;
    }
}
