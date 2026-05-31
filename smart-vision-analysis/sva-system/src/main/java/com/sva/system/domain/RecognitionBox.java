package com.sva.system.domain;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class RecognitionBox {
    private Long id;
    private Long resultId;
    private BigDecimal x;
    private BigDecimal y;
    private BigDecimal width;
    private BigDecimal height;
    private String label;
    private BigDecimal confidence;
    private Integer source;
    private Integer isDeleted;
    private Long updatedBy;
    private LocalDateTime updatedAt;
}
