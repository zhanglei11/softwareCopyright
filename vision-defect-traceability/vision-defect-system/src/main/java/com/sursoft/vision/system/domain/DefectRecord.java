package com.sursoft.vision.system.domain;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DefectRecord {
    private Long id;
    private String serialNo;
    private String batchNo;
    private Long lineId;
    private Long productId;
    private Long categoryId;
    private Integer level;
    private Integer result;
    private String shift;
    private LocalDateTime detectTime;
    private Integer disposeStatus;
    private String disposeRemark;
    private Long disposeBy;
    private LocalDateTime disposeAt;
    private Integer isDeleted;
    private LocalDateTime createdAt;
}
