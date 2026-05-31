package com.sursoft.vision.common.core;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.function.Supplier;

@RestController
public abstract class BaseController {

    protected <T> TableDataInfo<T> getDataTable(int pageNum, int pageSize, Supplier<List<T>> query) {
        PageHelper.startPage(pageNum, pageSize);
        List<T> list = query.get();
        PageInfo<T> pageInfo = new PageInfo<>(list);
        return TableDataInfo.of(pageInfo);
    }

    protected AjaxResult<Void> toAjax(int rows) {
        return rows > 0 ? AjaxResult.success() : AjaxResult.error("操作失败");
    }
}
