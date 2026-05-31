package com.sursoft.vision.system.domain;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AlertRecord {
    private Long id;
    private Long ruleId;
    private String alertContent;
    private LocalDateTime alertTime;
    private Integer handleStatus;
    private String handleRemark;
    private Long handleBy;
    private LocalDateTime handleAt;
    private LocalDateTime createdAt;
}
