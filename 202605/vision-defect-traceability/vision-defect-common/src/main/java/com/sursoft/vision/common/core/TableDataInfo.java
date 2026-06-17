package com.sursoft.vision.common.core;

import lombok.Data;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class TableDataInfo<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Integer code = 200;
    private String msg = "查询成功";
    private long total;
    private int page;
    private int pageSize;
    private List<T> records;
    private List<T> rows;

    public TableDataInfo(long total, List<T> records) {
        this.total = total;
        this.records = records;
        this.rows = records;
    }

    public static <T> TableDataInfo<T> of(com.github.pagehelper.PageInfo<T> pageInfo) {
        TableDataInfo<T> info = new TableDataInfo<>(pageInfo.getTotal(), pageInfo.getList());
        info.setPage(pageInfo.getPageNum());
        info.setPageSize(pageInfo.getPageSize());
        return info;
    }
}
