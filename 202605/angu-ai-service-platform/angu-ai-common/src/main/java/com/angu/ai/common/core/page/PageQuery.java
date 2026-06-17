package com.angu.ai.common.core.page;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "分页查询基础参数")
public class PageQuery {

    @Schema(description = "页码，从1起", defaultValue = "1")
    private Integer page = 1;

    @Schema(description = "每页条数", defaultValue = "20")
    private Integer size = 20;
}
