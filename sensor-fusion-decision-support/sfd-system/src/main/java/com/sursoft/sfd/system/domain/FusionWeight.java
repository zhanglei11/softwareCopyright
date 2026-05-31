package com.sursoft.sfd.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter
@Schema(description = "融合权重配置")
public class FusionWeight {
    @Schema(description = "权重ID") private Long id;
    @Schema(description = "所属方案ID") private Long schemeId;
    @Schema(description = "所属规则ID") private Long ruleId;
    @Schema(description = "数据源ID") private Long dsId;
    @Schema(description = "数据源名称（扩展）") private String dsName;
    @Schema(description = "权重值(0.00~1.00)") private BigDecimal weightValue;
    @Schema(description = "调整原因") private String adjustReason;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间") private LocalDateTime updatedAt;
    @Schema(description = "更新人ID") private Long updatedBy;
}
