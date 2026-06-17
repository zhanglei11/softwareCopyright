package com.vqcc.system.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "不合格品忽略请求")
public class DefectIgnoreReq {
    @NotNull(message = "不合格品ID不能为空")
    @Schema(description = "不合格品ID")
    private Long defectId;

    @NotBlank(message = "忽略原因不能为空")
    @Schema(description = "忽略原因")
    private String ignoreReason;
}
