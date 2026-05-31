package com.sursoft.iidp.common.core;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@Schema(description = "分页响应体")
public class TableDataInfo<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "状态码")
    private int code = 200;

    @Schema(description = "总记录数")
    private long total;

    @Schema(description = "数据列表")
    private List<T> rows;

    @Schema(description = "提示消息")
    private String msg = "查询成功";

    public TableDataInfo() {}

    public TableDataInfo(List<T> rows, long total) {
        this.rows = rows;
        this.total = total;
    }
}
