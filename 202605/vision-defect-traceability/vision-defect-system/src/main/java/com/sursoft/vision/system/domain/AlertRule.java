package com.sursoft.vision.system.domain;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AlertRule {
    private Long id;
    private String ruleName;
    private Long lineId;
    private Integer conditionType;
    private BigDecimal threshold;
    private Integer statCycle;
    private Integer alertLevel;
    private String notifyUserIds;
    private Integer status;
    private Integer isDeleted;
    private LocalDateTime createdAt;
    private Long createdBy;
    private LocalDateTime updatedAt;
    private Long updatedBy;
}
