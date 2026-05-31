package com.sursoft.sfd.system.domain;

import com.sursoft.sfd.common.core.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Schema(description = "融合规则")
public class FusionRule extends BaseEntity {
    @Schema(description = "所属方案ID") private Long schemeId;
    @Schema(description = "规则名称") private String ruleName;
    @Schema(description = "融合类型 WEIGHTED/VOTE/PRIORITY") private String fusionType;
    @Schema(description = "融合字段列表（JSON）") private String fusionFields;
    @Schema(description = "触发条件表达式") private String triggerCondition;
    @Schema(description = "执行顺序") private Integer sort;
}
