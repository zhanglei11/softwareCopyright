package com.imaging.scheduler.system.dto.req;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TaskAddReq {
    @NotBlank @Size(max=100)
    private String taskName;
    @NotNull
    private Long sceneId;
    @NotNull
    private Integer taskType;
    @NotNull
    private LocalDateTime planStartTime;
    private LocalDateTime planEndTime;
    @NotNull @Min(1)
    private Integer deviceCount;
    private Integer deviceTypeReq;
    @NotNull
    private Integer priority;
    @Size(max=500)
    private String description;
}
