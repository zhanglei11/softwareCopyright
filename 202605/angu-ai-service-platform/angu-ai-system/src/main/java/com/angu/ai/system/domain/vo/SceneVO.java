package com.angu.ai.system.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "场景 VO")
public class SceneVO {
    private Long id;
    private String name;
    private Long categoryId;
    private String categoryName;
    private String icon;
    private String description;
    private String usageGuide;
    private Long modelId;
    private String modelName;
    private Long kbId;
    private String systemPrompt;
    private String userPromptTpl;
    private String inputVariables;
    private Integer maxTokens;
    private BigDecimal temperature;
    private Integer multiTurn;
    private String status;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    private Boolean favorited;
}
