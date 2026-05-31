package com.angu.matcher.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "匹配规则配置")
public class MatchConfig {
    private Long id;
    private Integer skillWeight;
    private Integer eduWeight;
    private Integer expWeight;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedTime;
    private Long updaterId;
}
