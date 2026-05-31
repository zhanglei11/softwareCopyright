package com.angu.ai.system.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "会话 VO")
public class ConversationVO {
    private Long id;
    private Long sceneId;
    private String sceneName;
    private String title;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}
