package com.imaging.scheduler.system.domain.device;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DeviceParam {
    private Long id;
    private Long deviceId;
    private String paramKey;
    private String paramValue;
    private String paramDesc;
    private LocalDateTime updatedAt;
}
