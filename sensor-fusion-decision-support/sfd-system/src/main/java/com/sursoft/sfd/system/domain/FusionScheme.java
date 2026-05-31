package com.sursoft.sfd.system.domain;

import com.sursoft.sfd.common.core.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Schema(description = "融合方案")
public class FusionScheme extends BaseEntity {
    @Schema(description = "方案编号") private String schemeCode;
    @Schema(description = "方案名称") private String schemeName;
    @Schema(description = "参与场景列表（JSON数组）") private String sceneTypes;
    @Schema(description = "融合目标描述") private String fusionGoal;
    @Schema(description = "规则数量（查询扩展）") private Integer ruleCount;
    @Schema(description = "创建人名称（查询扩展）") private String createdByName;
}
