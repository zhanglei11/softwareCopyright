package com.angu.matcher.common.utils;

import com.angu.matcher.common.result.TableDataInfo;
import com.github.pagehelper.PageInfo;

import java.util.List;

public final class PageUtils {
    private PageUtils() {}

    public static <T> TableDataInfo<T> build(List<T> list) {
        PageInfo<T> info = new PageInfo<>(list);
        return new TableDataInfo<>(info.getTotal(), info.getPages(), list);
    }
}
