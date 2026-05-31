package com.imaging.scheduler.system.domain.dispatch;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DispatchConfig {
    private Long id;
    private Integer maxDevicesPerTask;
    private Integer taskTimeoutMinutes;
    private Integer autoDispatchEnabled;
    private String dispatchStrategy;
    private Integer alertThresholdMinutes;
    private LocalDateTime updatedAt;
}
