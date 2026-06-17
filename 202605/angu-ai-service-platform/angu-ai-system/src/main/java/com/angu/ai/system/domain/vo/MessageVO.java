package com.angu.ai.system.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "消息 VO")
public class MessageVO {
    private Long id;
    private String role;
    private String content;
    private Integer tokenCount;
    private LocalDateTime createdTime;
}
