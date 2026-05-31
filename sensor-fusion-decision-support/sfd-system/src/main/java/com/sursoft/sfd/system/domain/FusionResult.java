package com.sursoft.sfd.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter @Setter
@Schema(description = "融合结果")
public class FusionResult {
    @Schema(description = "结果ID") private Long id;
    @Schema(description = "结果编号") private String resultCode;
    @Schema(description = "融合方案ID") private Long schemeId;
    @Schema(description = "融合方案名称") private String schemeName;
    @Schema(description = "参与场景数") private Integer sceneCount;
    @Schema(description = "融合数据条数") private Integer dataRecordCount;
    @Schema(description = "结果状态 0异常 1成功") private Integer resultStatus;
    @Schema(description = "异常描述") private String errorMsg;
    @Schema(description = "各场景原始数据摘要（JSON）") private String rawDataSummary;
    @Schema(description = "融合后综合数据字段（JSON）") private String fusionData;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "融合执行时间") private LocalDateTime executedAt;
    @Schema(description = "执行人/系统") private Long createdBy;
}
