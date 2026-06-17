package com.angu.matcher.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "匹配结果")
public class MatchResult {
    private Long id;
    private Long positionId;
    private Long resumeId;
    private BigDecimal totalScore;
    private BigDecimal skillScore;
    private BigDecimal eduScore;
    private BigDecimal expScore;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime matchedAt;
    private String resumeName;
    private String resumePhone;
}
