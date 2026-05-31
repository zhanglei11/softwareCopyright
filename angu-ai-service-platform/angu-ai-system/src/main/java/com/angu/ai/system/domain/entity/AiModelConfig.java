package com.angu.ai.system.domain.entity;

import com.angu.ai.common.core.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "AI 模型配置")
public class AiModelConfig extends BaseEntity {

    @Schema(description = "显示名称，如：GPT-4o")
    private String modelName;

    @Schema(description = "API model ID")
    private String modelId;

    @Schema(description = "服务提供商")
    private String provider;

    @Schema(description = "API 地址")
    private String apiUrl;

    @Schema(description = "API 密钥（AES加密存储）")
    private String apiKeyEncrypted;

    @Schema(description = "最大上下文长度")
    private Integer maxContextTokens;

    @Schema(description = "状态：0 禁用 / 1 启用")
    private Integer status;

    @Schema(description = "备注")
    private String remark;
}
