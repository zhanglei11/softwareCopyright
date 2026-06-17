package com.sursoft.sfd.common.core;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.List;

@Schema(description = "分页响应体")
public class PageResult<T> implements Serializable {

    @Schema(description = "总记录数")
    private long total;
    @Schema(description = "当前页")
    private int page;
    @Schema(description = "每页数量")
    private int pageSize;
    @Schema(description = "数据列表")
    private List<T> records;

    public static <T> PageResult<T> of(long total, int page, int pageSize, List<T> records) {
        PageResult<T> r = new PageResult<>();
        r.total = total;
        r.page = page;
        r.pageSize = pageSize;
        r.records = records;
        return r;
    }

    public long getTotal() { return total; }
    public int getPage() { return page; }
    public int getPageSize() { return pageSize; }
    public List<T> getRecords() { return records; }
}
