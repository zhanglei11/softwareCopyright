package com.angu.ai.system.domain.query;

import com.angu.ai.common.core.page.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "会话查询参数")
public class ConversationQuery extends PageQuery {
    @Schema(description = "场景 ID")
    private Long sceneId;
    @Schema(description = "用户 ID（管理员视角）")
    private Long userId;
    @Schema(description = "关键字（会话标题）")
    private String keyword;
    @Schema(description = "开始时间")
    private String startTime;
    @Schema(description = "结束时间")
    private String endTime;
}
