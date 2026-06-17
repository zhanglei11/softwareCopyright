package com.angu.ai.system.domain.entity;

import com.angu.ai.common.core.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "AI 会话")
public class AiConversation extends BaseEntity {

    @Schema(description = "用户 ID")
    private Long userId;

    @Schema(description = "场景 ID（NULL 表示通用对话）")
    private Long sceneId;

    @Schema(description = "会话标题")
    private String title;

    @Schema(description = "是否删除：0 正常 / 1 已删除")
    private Integer deleted;
}
