package com.angu.ai.system.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "AI 模型配置请求")
public class AiModelConfigDTO {

    @NotBlank
    @Schema(description = "显示名称")
    private String modelName;

    @NotBlank
    @Schema(description = "API model ID")
    private String modelId;

    @NotBlank
    @Schema(description = "服务提供商")
    private String provider;

    @NotBlank
    @Schema(description = "API 地址")
    private String apiUrl;

    @NotBlank
    @Schema(description = "API 密钥（明文，后端加密存储）")
    private String apiKey;

    @Schema(description = "最大上下文长度")
    private Integer maxContextTokens;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "状态：0 禁用 / 1 启用，默认 1")
    private Integer status;
}
