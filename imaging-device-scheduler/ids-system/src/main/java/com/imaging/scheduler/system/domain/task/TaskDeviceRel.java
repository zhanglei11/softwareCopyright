package com.imaging.scheduler.system.domain.task;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskDeviceRel {
    private Long id;
    private Long taskId;
    private Long deviceId;
    private Integer status;
    private LocalDateTime assignedAt;
    private Long assignedBy;
    private LocalDateTime releasedAt;
}
