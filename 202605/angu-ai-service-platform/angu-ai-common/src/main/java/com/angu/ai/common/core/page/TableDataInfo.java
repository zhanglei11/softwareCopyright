package com.angu.ai.common.core.page;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "分页响应数据")
public class TableDataInfo<T> implements Serializable {

    @Schema(description = "总记录数")
    private long total;

    @Schema(description = "总页数")
    private int pages;

    @Schema(description = "数据列表")
    private List<T> list;

    public static <T> TableDataInfo<T> of(long total, int pages, List<T> list) {
        TableDataInfo<T> result = new TableDataInfo<>();
        result.setTotal(total);
        result.setPages(pages);
        result.setList(list);
        return result;
    }
}
