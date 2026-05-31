package com.sva.system.domain;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ModelVersion {
    private Long id;
    private String modelName;
    private String versionNo;
    private String sceneDesc;
    private String supportLabels;
    private LocalDate releaseDate;
    private Integer status;
    private String remark;
    private Long createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer deleted;
}
