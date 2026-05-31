package com.sva.common.core.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sva.common.core.domain.TableDataInfo;

import java.util.List;

public class BaseController {

    protected void startPage(int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
    }

    protected <T> TableDataInfo getDataTable(List<T> list) {
        PageInfo<T> pageInfo = new PageInfo<>(list);
        TableDataInfo info = new TableDataInfo(list, pageInfo.getTotal());
        info.setPage(pageInfo.getPageNum());
        info.setPageSize(pageInfo.getPageSize());
        return info;
    }
}
