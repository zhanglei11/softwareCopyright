package com.imaging.scheduler.system.domain.device;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DeviceFaultRecord {
    private Long id;
    private Long deviceId;
    private String faultDesc;
    private Integer faultType;
    private Long reportedBy;
    private LocalDateTime faultAt;
    private LocalDateTime recoveredAt;
    private Integer isResolved;
}
