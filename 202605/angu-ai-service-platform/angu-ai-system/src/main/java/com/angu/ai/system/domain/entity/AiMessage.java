package com.angu.ai.system.domain.entity;

import com.angu.ai.common.core.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "AI 消息")
public class AiMessage extends BaseEntity {

    @Schema(description = "会话 ID")
    private Long conversationId;

    @Schema(description = "消息角色：USER / ASSISTANT / SYSTEM")
    private String role;

    @Schema(description = "消息内容（Markdown）")
    private String content;

    @Schema(description = "本条消息 Token 数")
    private Integer tokenCount;
}
