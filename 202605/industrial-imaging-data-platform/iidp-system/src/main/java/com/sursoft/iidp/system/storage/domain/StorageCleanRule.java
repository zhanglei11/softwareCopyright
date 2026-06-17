package com.sursoft.iidp.system.storage.domain;

import com.sursoft.iidp.common.core.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data @EqualsAndHashCode(callSuper = true)
@Schema(description = "数据清理规则")
public class StorageCleanRule extends BaseEntity {
    private Long id;
    private String ruleName;
    private String targetDir;
    @Schema(description = "LAST_ACCESS_DAYS/BEFORE_DATE/FILE_SIZE") private String conditionType;
    private String conditionValue;
    @Schema(description = "MANUAL/SCHEDULED") private String executeType;
    private String cronExpression;
    @Schema(description = "DELETE/ARCHIVE") private String afterAction;
    private String archiveDir;
    private Integer status;
    private String remark;
}
