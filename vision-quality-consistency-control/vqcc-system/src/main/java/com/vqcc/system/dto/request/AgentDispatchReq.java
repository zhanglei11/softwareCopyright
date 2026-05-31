package com.vqcc.system.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "调度任务给智能体请求")
public class AgentDispatchReq {
    @NotNull(message = "智能体ID不能为空")
    private Long agentId;
    @NotNull(message = "检测任务ID不能为空")
    private Long taskId;
}
