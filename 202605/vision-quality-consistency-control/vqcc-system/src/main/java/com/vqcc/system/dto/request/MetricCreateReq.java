package com.vqcc.system.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Schema(description = "新增质量指标请求")
public class MetricCreateReq {
    @NotBlank(message = "指标名称不能为空")
    @Schema(description = "指标名称")
    private String metricName;

    @NotNull(message = "指标类型不能为空")
    @Schema(description = "指标类型 0-数值型 1-等级型")
    private Integer metricType;

    @Schema(description = "计量单位")
    private String unit;

    @Schema(description = "数值型下限")
    private BigDecimal minValue;

    @Schema(description = "数值型上限")
    private BigDecimal maxValue;

    @Schema(description = "等级描述JSON")
    private String levelDesc;

    @Schema(description = "重要性 0-低 1-中 2-高，默认1")
    private Integer importance = 1;

    @Schema(description = "备注")
    private String remark;
}
