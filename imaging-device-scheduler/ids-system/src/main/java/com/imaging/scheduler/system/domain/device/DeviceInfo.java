package com.imaging.scheduler.system.domain.device;

import com.imaging.scheduler.common.core.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class DeviceInfo extends BaseEntity {
    private Long id;
    private String deviceCode;
    private String deviceName;
    private Integer deviceType;
    private String modelSpec;
    private Long sceneId;
    private String ipAddress;
    private String location;
    private Long registeredBy;
    private Integer status;
    private LocalDateTime lastHeartbeat;
    private Integer isDeleted;
    // 关联字段（非DB列）
    private String sceneName;
}
