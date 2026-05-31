package com.sva.system.domain;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class RecognitionTask {
    private Long id;
    private String taskNo;
    private String taskName;
    private Long modelVersionId;
    private String modelVersionNo;
    private BigDecimal confidenceThreshold;
    private Integer selectMode;
    private Integer totalCount;
    private Integer processedCount;
    private Integer successCount;
    private Integer failCount;
    private BigDecimal avgConfidence;
    private Integer status;
    private String failReason;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;
    private String remark;
    private Long createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
