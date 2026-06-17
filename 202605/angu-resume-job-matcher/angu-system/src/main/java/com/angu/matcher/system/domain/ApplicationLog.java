package com.angu.matcher.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "投递操作日志")
public class ApplicationLog {
    private Long id;
    private Long applicationId;
    private String fromStatus;
    private String toStatus;
    private Long operatorId;
    private String operatorName;
    private String remark;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;
}
