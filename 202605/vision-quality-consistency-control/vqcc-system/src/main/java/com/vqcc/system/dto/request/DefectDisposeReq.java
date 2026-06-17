package com.vqcc.system.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "不合格品处置请求")
public class DefectDisposeReq {
    @NotNull(message = "不合格品ID不能为空")
    @Schema(description = "不合格品ID")
    private Long defectId;

    @NotNull(message = "处置方案不能为空")
    @Schema(description = "处置方案 1-重新采集 2-参数调整 3-设备维护 4-接受")
    private Integer disposePlan;

    @NotBlank(message = "处置结果说明不能为空")
    @Schema(description = "处置结果说明")
    private String resultDesc;
}
