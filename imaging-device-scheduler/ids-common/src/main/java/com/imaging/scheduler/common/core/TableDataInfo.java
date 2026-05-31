package com.imaging.scheduler.common.core;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页响应结果
 */
@Data
public class TableDataInfo<T> implements Serializable {

    private Integer code;
    private String message;
    private PageData<T> data;

    @Data
    public static class PageData<T> {
        private long total;
        private int page;
        private int pageSize;
        private List<T> records;
    }

    public static <T> TableDataInfo<T> success(long total, int page, int pageSize, List<T> records) {
        TableDataInfo<T> info = new TableDataInfo<>();
        info.setCode(200);
        info.setMessage("ok");
        PageData<T> pageData = new PageData<>();
        pageData.setTotal(total);
        pageData.setPage(page);
        pageData.setPageSize(pageSize);
        pageData.setRecords(records);
        info.setData(pageData);
        return info;
    }
}
