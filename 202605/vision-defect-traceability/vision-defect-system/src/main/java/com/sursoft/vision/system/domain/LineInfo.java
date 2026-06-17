package com.sursoft.vision.system.domain;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class LineInfo {
    private Long id;
    private String lineNo;
    private String lineName;
    private String workshop;
    private Long managerId;
    private Integer status;
    private String remark;
    private Integer isDeleted;
    private LocalDateTime createdAt;
    private Long createdBy;
    private LocalDateTime updatedAt;
    private Long updatedBy;
}
