package com.angu.ai.system.domain.query;

import com.angu.ai.common.core.page.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "场景查询参数")
public class SceneQuery extends PageQuery {
    @Schema(description = "分类 ID")
    private Long categoryId;
    @Schema(description = "状态：DRAFT / ONLINE / OFFLINE")
    private String status;
    @Schema(description = "关键字")
    private String keyword;
}
