package com.angu.ai.system.domain.entity;

import com.angu.ai.common.core.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "知识库")
public class KbKnowledgeBase extends BaseEntity {

    @Schema(description = "知识库名称")
    private String name;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "文档数量（冗余统计）")
    private Integer docCount;

    @Schema(description = "是否删除：0 正常 / 1 已删除")
    private Integer deleted;

    @Schema(description = "创建人 ID")
    private Long creatorId;
}
