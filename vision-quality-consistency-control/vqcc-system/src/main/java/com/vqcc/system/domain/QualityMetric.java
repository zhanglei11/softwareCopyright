package com.vqcc.system.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "质量指标")
public class QualityMetric {
    private Long id;
    @Schema(description = "指标编号，QM-{序号}")
    private String metricCode;
    @Schema(description = "指标名称")
    private String metricName;
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
    @Schema(description = "重要性 0-低 1-中 2-高")
    private Integer importance;
    private String remark;
    @Schema(description = "状态 0-停用 1-启用")
    private Integer status;
    private Integer isDeleted;
    private LocalDateTime createdAt;
    private Long createdBy;
    private LocalDateTime updatedAt;
    private Long updatedBy;
}
