package com.sursoft.vision.system.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DefectRecordVO {
    private Long id;
    private String serialNo;
    private String batchNo;
    private String lineName;
    private String productTypeName;
    private String categoryName;
    private Integer level;
    private String levelLabel;
    private Integer result;
    private String resultLabel;
    private String shift;
    private LocalDateTime detectTime;
    private Integer disposeStatus;
    private String disposeStatusLabel;
}
