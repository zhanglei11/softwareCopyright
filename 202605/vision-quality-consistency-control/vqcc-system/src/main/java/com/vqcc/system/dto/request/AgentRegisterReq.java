package com.vqcc.system.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "注册智能体请求")
public class AgentRegisterReq {
    @NotBlank(message = "智能体名称不能为空")
    private String agentName;
    @NotBlank(message = "智能体编码不能为空")
    private String agentCode;
    @NotNull(message = "类型不能为空")
    @Schema(description = "1=视觉检测 2=尺寸测量 3=缺陷识别")
    private Integer agentType;
    private String endpointUrl;
    private String authToken;
    private String remark;
}
