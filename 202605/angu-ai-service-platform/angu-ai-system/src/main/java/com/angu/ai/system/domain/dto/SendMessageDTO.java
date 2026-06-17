package com.angu.ai.system.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "发送消息请求")
public class SendMessageDTO {

    @NotBlank
    @Schema(description = "消息内容")
    private String content;
}
