package com.vqcc.system.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import java.util.List;

@Data
@Schema(description = "新增标准模板请求")
public class TemplateCreateReq {
    @NotBlank(message = "模板名称不能为空")
    @Schema(description = "模板名称")
    private String templateName;

    @Schema(description = "适用场景")
    private String applicableScene;

    @Schema(description = "备注")
    private String remark;

    @NotEmpty(message = "至少选择一个质量指标")
    @Schema(description = "质量指标ID列表")
    private List<Long> metricIds;
}
