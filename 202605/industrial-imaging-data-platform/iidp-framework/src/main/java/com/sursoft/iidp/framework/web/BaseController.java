package com.sursoft.iidp.framework.web;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sursoft.iidp.common.core.PageDomain;
import com.sursoft.iidp.common.core.TableDataInfo;
import org.springframework.util.StringUtils;
import java.util.List;

public class BaseController {

    protected void startPage(PageDomain pageDomain) {
        int pageNum = pageDomain.getPageNum() != null ? pageDomain.getPageNum() : 1;
        int pageSize = pageDomain.getPageSize() != null ? pageDomain.getPageSize() : 10;
        String orderBy = pageDomain.getOrderByColumn();
        if (StringUtils.hasText(orderBy)) {
            String isAsc = "desc".equalsIgnoreCase(pageDomain.getIsAsc()) ? "desc" : "asc";
            PageHelper.startPage(pageNum, pageSize, orderBy + " " + isAsc);
        } else {
            PageHelper.startPage(pageNum, pageSize);
        }
    }

    protected <T> TableDataInfo<T> getDataTable(List<T> list) {
        PageInfo<T> pageInfo = new PageInfo<>(list);
        return new TableDataInfo<>(list, pageInfo.getTotal());
    }
}
