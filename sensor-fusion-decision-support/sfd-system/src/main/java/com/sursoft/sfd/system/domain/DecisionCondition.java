package com.sursoft.sfd.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter @Setter
@Schema(description = "决策条件")
public class DecisionCondition {
    @Schema(description = "条件ID") private Long id;
    @Schema(description = "条件名称") private String conditionName;
    @Schema(description = "作用字段") private String conditionField;
    @Schema(description = "运算符 GT/LT/EQ/BETWEEN/CONTAINS") private String operator;
    @Schema(description = "阈值") private String thresholdValue;
    @Schema(description = "阈值2（BETWEEN时使用）") private String thresholdValue2;
    @Schema(description = "条件用途说明") private String description;
    @Schema(description = "逻辑删除") private Integer isDeleted;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间") private LocalDateTime createdAt;
    @Schema(description = "创建人ID") private Long createdBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间") private LocalDateTime updatedAt;
    @Schema(description = "更新人ID") private Long updatedBy;
}
