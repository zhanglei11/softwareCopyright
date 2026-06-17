package com.sursoft.sfd.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter @Setter
@Schema(description = "决策结果")
public class DecisionResult {
    @Schema(description = "决策结果ID") private Long id;
    @Schema(description = "结果编号") private String resultCode;
    @Schema(description = "关联融合结果ID") private Long fusionResultId;
    @Schema(description = "触发的决策规则ID") private Long ruleId;
    @Schema(description = "规则名称（冗余）") private String ruleName;
    @Schema(description = "融合方案ID（冗余）") private Long schemeId;
    @Schema(description = "融合方案名称（冗余）") private String schemeName;
    @Schema(description = "决策输出内容") private String decisionOutput;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "触发时间") private LocalDateTime triggeredAt;
    // 追溯扩展字段
    @Schema(description = "关联融合结果（追溯）") private FusionResult fusionResult;
}
