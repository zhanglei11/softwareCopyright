package com.imaging.scheduler.system.dto.req;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class DeviceAddReq {
    @NotBlank @Size(max=50)
    private String deviceName;
    @NotNull
    private Integer deviceType;
    @NotBlank @Size(max=100)
    private String modelSpec;
    @NotNull
    private Long sceneId;
    @NotBlank @Pattern(regexp = "^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$", message = "IP地址格式不正确")
    private String ipAddress;
    @Size(max=200)
    private String location;
}
