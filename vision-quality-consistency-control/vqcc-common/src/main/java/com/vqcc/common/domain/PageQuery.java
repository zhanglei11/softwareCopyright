package com.vqcc.common.domain;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "分页查询基类")
public class PageQuery {

    @Schema(description = "页码，默认1")
    private int pageNum = 1;

    @Schema(description = "每页数量，默认10")
    private int pageSize = 10;

    public int getPageNum() { return pageNum; }
    public void setPageNum(int pageNum) { this.pageNum = pageNum; }
    public int getPageSize() { return pageSize; }
    public void setPageSize(int pageSize) { this.pageSize = pageSize; }
}
