package com.sursoft.iidp.system.process.domain;

import com.sursoft.iidp.common.core.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data @EqualsAndHashCode(callSuper = true)
@Schema(description = "处理任务")
public class ProcessTask extends BaseEntity {
    private Long id;
    private String taskCode;
    private String taskName;
    private String inputDir;
    @Schema(description = "IMAGE_COMPRESS/FORMAT_CONVERT/RESOLUTION_RESIZE/BATCH_RENAME/QUALITY_FILTER")
    private String processType;
    private String processParams;
    private String outputDir;
    @Schema(description = "MANUAL/SCHEDULED") private String executeType;
    private String cronExpression;
    @Schema(description = "0停用1启用2执行中3已终止") private Integer status;
}
