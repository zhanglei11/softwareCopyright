package com.vqcc.system.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "检测任务")
public class QualityTask {
    private Long id;
    @Schema(description = "任务编号，TASK-{yyyyMMdd}-{序号}")
    private String taskCode;
    @Schema(description = "任务名称")
    private String taskName;
    @Schema(description = "检测对象描述")
    private String detectionTarget;
    @Schema(description = "关联标准模板ID")
    private Long templateId;
    @Schema(description = "影像总数量")
    private Integer imageCount;
    @Schema(description = "计划执行时间")
    private LocalDateTime planExecuteTime;
    @Schema(description = "优先级 0-低 1-中 2-高")
    private Integer priority;
    private String remark;
    @Schema(description = "状态 1-待执行 2-执行中 3-已完成 4-已取消")
    private Integer status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer qualifiedCount;
    private Integer unqualifiedCount;
    private BigDecimal qualifiedRate;
    private Integer isDeleted;
    private LocalDateTime createdAt;
    private Long createdBy;
    private LocalDateTime updatedAt;
    private Long updatedBy;
    // 关联查询
    private String templateName;
}
