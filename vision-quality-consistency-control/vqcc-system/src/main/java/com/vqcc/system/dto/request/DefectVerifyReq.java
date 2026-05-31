package com.vqcc.system.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "不合格品验证请求")
public class DefectVerifyReq {
    @NotNull(message = "处置记录ID不能为空")
    @Schema(description = "处置记录ID")
    private Long disposeId;

    @NotNull(message = "验证状态不能为空")
    @Schema(description = "验证状态 1-已验证合格 2-验证不合格")
    private Integer verifyStatus;

    @Schema(description = "验证备注")
    private String verifyComment;
}
