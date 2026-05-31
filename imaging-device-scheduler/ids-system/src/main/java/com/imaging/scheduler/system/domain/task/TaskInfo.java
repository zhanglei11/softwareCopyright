package com.imaging.scheduler.system.domain.task;

import com.imaging.scheduler.common.core.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class TaskInfo extends BaseEntity {
    private Long id;
    private String taskCode;
    private String taskName;
    private Long sceneId;
    private Integer taskType;
    private LocalDateTime planStartTime;
    private LocalDateTime planEndTime;
    private LocalDateTime actualStartTime;
    private LocalDateTime actualEndTime;
    private Integer deviceCount;
    private Integer deviceTypeReq;
    private Integer priority;
    private String description;
    private Integer status;
    private Integer isDeleted;
}
