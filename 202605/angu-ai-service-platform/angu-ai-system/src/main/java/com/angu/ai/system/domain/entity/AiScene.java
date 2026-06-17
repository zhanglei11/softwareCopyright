package com.angu.ai.system.domain.entity;

import com.angu.ai.common.core.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "AI 场景")
public class AiScene extends BaseEntity {

    @Schema(description = "场景名称")
    private String name;

    @Schema(description = "场景分类 ID")
    private Long categoryId;

    @Schema(description = "场景图标")
    private String icon;

    @Schema(description = "场景描述")
    private String description;

    @Schema(description = "使用说明（富文本）")
    private String usageGuide;

    @Schema(description = "关联模型 ID")
    private Long modelId;

    @Schema(description = "关联知识库 ID（可为 NULL）")
    private Long kbId;

    @Schema(description = "系统提示词")
    private String systemPrompt;

    @Schema(description = "用户提示词模板")
    private String userPromptTpl;

    @Schema(description = "输入变量定义（JSON）")
    private String inputVariables;

    @Schema(description = "最大输出 Token，默认 2048")
    private Integer maxTokens;

    @Schema(description = "温度参数，默认 0.70")
    private BigDecimal temperature;

    @Schema(description = "是否支持多轮对话：0 单轮 / 1 多轮")
    private Integer multiTurn;

    @Schema(description = "场景状态：DRAFT / ONLINE / OFFLINE")
    private String status;

    @Schema(description = "是否删除：0 正常 / 1 已删除")
    private Integer deleted;

    @Schema(description = "创建人 ID")
    private Long creatorId;
}
