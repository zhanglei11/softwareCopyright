package com.sva.system.domain;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RecognitionResult {
    private Long id;
    private Long taskId;
    private Long imageId;
    private Integer reviewStatus;
    private Long reviewedBy;
    private LocalDateTime reviewedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    // 关联展示字段
    private String imageNo;
    private String fileName;
    private String taskName;
    private Integer boxCount;
}
