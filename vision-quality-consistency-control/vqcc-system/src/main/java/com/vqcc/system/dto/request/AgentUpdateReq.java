package com.vqcc.system.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "更新智能体请求")
public class AgentUpdateReq {
    @NotNull(message = "ID不能为空")
    private Long id;
    private String agentName;
    private Integer agentType;
    private String endpointUrl;
    private String authToken;
    private String remark;
}
