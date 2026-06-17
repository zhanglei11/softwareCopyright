package com.sva.common.core.domain;

import java.io.Serializable;
import java.util.List;

public class TableDataInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private long total;
    private int page;
    private int pageSize;
    private List<?> records;
    private int code = 200;
    private String message = "查询成功";

    public TableDataInfo() {}

    public TableDataInfo(List<?> records, long total) {
        this.records = records;
        this.total = total;
    }

    public long getTotal() { return total; }
    public void setTotal(long total) { this.total = total; }
    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }
    public int getPageSize() { return pageSize; }
    public void setPageSize(int pageSize) { this.pageSize = pageSize; }
    public List<?> getRecords() { return records; }
    public void setRecords(List<?> records) { this.records = records; }
    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
