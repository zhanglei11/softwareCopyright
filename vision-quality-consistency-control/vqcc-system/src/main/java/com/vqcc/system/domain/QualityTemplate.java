package com.vqcc.system.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description = "质量标准模板")
public class QualityTemplate {
    private Long id;
    @Schema(description = "模板编号，QT-{序号}")
    private String templateCode;
    @Schema(description = "模板名称")
    private String templateName;
    @Schema(description = "适用场景")
    private String applicableScene;
    private String remark;
    @Schema(description = "状态 0-停用 1-启用")
    private Integer status;
    private Integer isDeleted;
    private LocalDateTime createdAt;
    private Long createdBy;
    private LocalDateTime updatedAt;
    private Long updatedBy;
    // 指标数量（列表查询时填充）
    private Integer metricCount;
    // 关联指标（查询时填充）
    private List<QualityMetric> metrics;
}
