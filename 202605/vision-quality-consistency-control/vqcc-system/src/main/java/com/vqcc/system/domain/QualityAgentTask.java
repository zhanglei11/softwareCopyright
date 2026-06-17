package com.vqcc.system.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "智能体任务分配记录")
public class QualityAgentTask {
    private Long id;
    @Schema(description = "智能体ID")
    private Long agentId;
    @Schema(description = "检测任务ID")
    private Long taskId;
    @Schema(description = "调度状态：0=待执行 1=执行中 2=已完成 3=失败")
    private Integer dispatchStatus;
    private LocalDateTime dispatchAt;
    private LocalDateTime completeAt;
    @Schema(description = "执行结果摘要")
    private String resultSummary;
    private Long createdBy;
    private LocalDateTime createdAt;
    // 关联字段
    private String agentName;
    private String agentCode;
    private String taskName;
    private String taskCode;
}
