package com.sursoft.iidp.system.process.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "处理执行记录")
public class ProcessExecution {
    private Long id;
    private String execCode;
    private Long taskId;
    private String taskName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") private LocalDateTime startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") private LocalDateTime endTime;
    private Integer totalCount;
    private Integer successCount;
    private Integer failCount;
    private Long outputSizeBytes;
    @Schema(description = "RUNNING/COMPLETED/FAILED/TERMINATED") private String executeStatus;
    private String failFileList;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") private LocalDateTime createdAt;
}
