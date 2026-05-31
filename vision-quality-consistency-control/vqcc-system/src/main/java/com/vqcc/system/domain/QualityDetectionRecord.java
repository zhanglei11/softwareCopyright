package com.vqcc.system.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "检测记录")
public class QualityDetectionRecord {
    private Long id;
    @Schema(description = "关联任务ID")
    private Long taskId;
    @Schema(description = "影像文件名或编号")
    private String imageId;
    @Schema(description = "各指标实测值JSON")
    private String measuredValues;
    @Schema(description = "是否合格 0-不合格 1-合格")
    private Integer isQualified;
    @Schema(description = "超标指标详情JSON")
    private String exceededMetrics;
    @Schema(description = "检测时间")
    private LocalDateTime detectedAt;
}
