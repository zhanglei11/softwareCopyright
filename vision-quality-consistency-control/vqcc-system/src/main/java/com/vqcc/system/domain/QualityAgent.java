package com.vqcc.system.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "AI检测智能体")
public class QualityAgent {
    private Long id;
    @Schema(description = "智能体名称")
    private String agentName;
    @Schema(description = "智能体编码，唯一标识")
    private String agentCode;
    @Schema(description = "类型：1=视觉检测 2=尺寸测量 3=缺陷识别")
    private Integer agentType;
    @Schema(description = "接入端点URL")
    private String endpointUrl;
    @Schema(description = "认证Token")
    private String authToken;
    @Schema(description = "状态：0=离线 1=空闲 2=运行中")
    private Integer status;
    @Schema(description = "最后心跳时间")
    private LocalDateTime lastHeartbeat;
    private String remark;
    private Integer isDeleted;
    private LocalDateTime createdAt;
    private Long createdBy;
    private LocalDateTime updatedAt;
    private Long updatedBy;
}
