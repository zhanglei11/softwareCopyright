package com.vqcc.system.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "提交检测记录请求")
public class DetectionRecordSubmitReq {
    @NotNull(message = "任务ID不能为空")
    @Schema(description = "任务ID")
    private Long taskId;

    @NotBlank(message = "影像标识不能为空")
    @Schema(description = "影像文件名或编号")
    private String imageId;

    @NotBlank(message = "实测值不能为空")
    @Schema(description = "各指标实测值JSON，如 [{\"metricId\":1,\"value\":165.3}]")
    private String measuredValues;
}
