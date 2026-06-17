package com.sursoft.sfd.system.domain;

import com.sursoft.sfd.common.core.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Schema(description = "决策规则")
public class DecisionRule extends BaseEntity {
    @Schema(description = "规则编号") private String ruleCode;
    @Schema(description = "规则名称") private String ruleName;
    @Schema(description = "关联融合方案ID") private Long schemeId;
    @Schema(description = "关联融合方案名称（扩展）") private String schemeName;
    @Schema(description = "触发条件表达式（JSON）") private String triggerCondition;
    @Schema(description = "决策输出内容") private String decisionOutput;
    @Schema(description = "优先级（数值越小越高）") private Integer priority;
}
