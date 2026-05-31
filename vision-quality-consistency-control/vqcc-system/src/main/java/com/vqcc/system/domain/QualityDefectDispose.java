package com.vqcc.system.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "不合格品处置记录")
public class QualityDefectDispose {
    private Long id;
    @Schema(description = "关联不合格品ID")
    private Long defectId;
    @Schema(description = "处置方案 1-重新采集 2-参数调整 3-设备维护 4-接受")
    private Integer disposePlan;
    @Schema(description = "处置人ID")
    private Long operatorId;
    @Schema(description = "处置时间")
    private LocalDateTime operateAt;
    @Schema(description = "处置结果说明")
    private String resultDesc;
    @Schema(description = "验证状态 0-待验证 1-已验证合格 2-验证不合格")
    private Integer verifyStatus;
    private String verifyComment;
    private LocalDateTime verifyAt;
    // 关联
    private String operatorName;
}
