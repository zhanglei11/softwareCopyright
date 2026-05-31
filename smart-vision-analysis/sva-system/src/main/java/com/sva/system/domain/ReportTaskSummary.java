package com.sva.system.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ReportTaskSummary {
    private Long id;
    private Long taskId;
    private Integer totalImages;
    private Integer successCount;
    private Integer failCount;
    private BigDecimal avgConfidence;
    private BigDecimal minConfidence;
    private BigDecimal maxConfidence;
    private Integer lowConfidenceCount;
    @JsonRawValue
    @JsonProperty("categoryCount")
    private String categoryStats;
    @JsonRawValue
    private String confidenceDistribution;
    private LocalDateTime generatedAt;
}
