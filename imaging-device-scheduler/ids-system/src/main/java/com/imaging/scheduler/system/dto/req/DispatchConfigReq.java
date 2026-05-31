package com.imaging.scheduler.system.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "调度配置更新请求")
public class DispatchConfigReq {
    @Schema(description = "每任务最大设备数")
    private Integer maxDevicesPerTask;
    @Schema(description = "任务超时分钟")
    private Integer taskTimeoutMinutes;
    @Schema(description = "是否启用自动调度 0=否 1=是")
    private Integer autoDispatchEnabled;
    @Schema(description = "调度策略 MANUAL/AUTO")
    private String dispatchStrategy;
    @Schema(description = "预警阈值分钟")
    private Integer alertThresholdMinutes;
}
