package com.vqcc.system.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "不合格品")
public class QualityDefect {
    private Long id;
    @Schema(description = "记录编号，DEF-{yyyyMMdd}-{序号}")
    private String defectCode;
    @Schema(description = "关联任务ID")
    private Long taskId;
    @Schema(description = "影像标识")
    private String imageId;
    @Schema(description = "超标指标信息JSON")
    private String exceededMetrics;
    @Schema(description = "各超标指标实测值JSON")
    private String exceededValues;
    @Schema(description = "各指标标准范围快照JSON")
    private String standardRanges;
    @Schema(description = "发现时间")
    private LocalDateTime foundAt;
    @Schema(description = "处置状态 1-待处置 2-处置中 3-已处置 4-已忽略")
    private Integer disposeStatus;
    @Schema(description = "验证状态 0-待验证 1-已验证合格 2-验证不合格")
    private Integer verifyStatus;
    private String ignoreReason;
    private Integer isDeleted;
    private LocalDateTime createdAt;
    private Long createdBy;
    private LocalDateTime updatedAt;
    private Long updatedBy;
    // 关联查询
    private String taskCode;
    private String taskName;
}
