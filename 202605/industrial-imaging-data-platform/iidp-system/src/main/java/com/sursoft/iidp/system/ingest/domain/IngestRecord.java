package com.sursoft.iidp.system.ingest.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "接入记录")
public class IngestRecord {
    private Long id;
    private String recordCode;
    private Long taskId;
    private String taskName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") private LocalDateTime startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") private LocalDateTime endTime;
    private Integer costSeconds;
    private Integer ingestCount;
    private Long dataSizeBytes;
    @Schema(description = "状态:SUCCESS/PARTIAL/FAILED") private String executeStatus;
    private String failReason;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") private LocalDateTime createdAt;
}
