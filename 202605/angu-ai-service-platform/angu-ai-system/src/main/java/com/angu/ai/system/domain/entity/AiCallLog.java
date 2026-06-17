package com.angu.ai.system.domain.entity;

import com.angu.ai.common.core.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "AI 调用日志")
public class AiCallLog extends BaseEntity {

    @Schema(description = "用户 ID")
    private Long userId;

    @Schema(description = "场景 ID")
    private Long sceneId;

    @Schema(description = "模型配置 ID")
    private Long modelId;

    @Schema(description = "会话 ID")
    private Long conversationId;

    @Schema(description = "输入 Token 数")
    private Integer promptTokens;

    @Schema(description = "输出 Token 数")
    private Integer completionTokens;

    @Schema(description = "总 Token 数")
    private Integer totalTokens;

    @Schema(description = "首字延迟（毫秒）")
    private Integer latencyMs;

    @Schema(description = "是否成功：0 失败 / 1 成功")
    private Integer success;

    @Schema(description = "错误信息")
    private String errorMsg;

    @Schema(description = "调用时间")
    private LocalDateTime callTime;
}
