package com.angu.ai.system.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "知识库请求")
public class KbDTO {

    @NotBlank
    @Schema(description = "知识库名称")
    private String name;

    @Schema(description = "描述")
    private String description;
}
