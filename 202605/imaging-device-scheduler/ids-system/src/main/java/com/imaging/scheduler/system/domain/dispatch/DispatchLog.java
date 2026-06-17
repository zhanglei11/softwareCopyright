package com.imaging.scheduler.system.domain.dispatch;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DispatchLog {
    private Long id;
    private Long taskId;
    private String taskName;
    private String action;
    private String actionDesc;
    private String deviceIds;
    private Long operatorId;
    private String operatorName;
    private LocalDateTime createdAt;
}
