package com.vqcc.system.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "创建检测任务请求")
public class TaskCreateReq {
    @NotBlank(message = "任务名称不能为空")
    @Schema(description = "任务名称")
    private String taskName;

    @NotBlank(message = "检测对象不能为空")
    @Schema(description = "检测对象描述")
    private String detectionTarget;

    @NotNull(message = "标准模板ID不能为空")
    @Schema(description = "标准模板ID")
    private Long templateId;

    @Min(value = 1, message = "影像数量至少为1")
    @Schema(description = "影像总数量")
    private Integer imageCount;

    @Schema(description = "计划执行时间")
    private LocalDateTime planExecuteTime;

    @Schema(description = "优先级 0-低 1-中 2-高，默认1")
    private Integer priority = 1;

    @Schema(description = "备注")
    private String remark;
}
