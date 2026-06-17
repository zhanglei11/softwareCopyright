package com.angu.ai.system.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "AI 场景配置请求")
public class AiSceneDTO {

    @NotBlank
    @Schema(description = "场景名称")
    private String name;

    @NotNull
    @Schema(description = "场景分类 ID")
    private Long categoryId;

    @Schema(description = "场景图标")
    private String icon;

    @NotBlank
    @Schema(description = "场景描述")
    private String description;

    @Schema(description = "使用说明")
    private String usageGuide;

    @NotNull
    @Schema(description = "关联模型 ID")
    private Long modelId;

    @Schema(description = "关联知识库 ID")
    private Long kbId;

    @NotBlank
    @Schema(description = "系统提示词")
    private String systemPrompt;

    @Schema(description = "用户提示词模板")
    private String userPromptTpl;

    @Schema(description = "输入变量定义（JSON）")
    private String inputVariables;

    @Schema(description = "最大输出 Token")
    private Integer maxTokens = 2048;

    @DecimalMin("0.00") @DecimalMax("1.00")
    @Schema(description = "温度参数")
    private BigDecimal temperature = new BigDecimal("0.70");

    @NotNull
    @Schema(description = "是否多轮：0 单轮 / 1 多轮")
    private Integer multiTurn;
}
