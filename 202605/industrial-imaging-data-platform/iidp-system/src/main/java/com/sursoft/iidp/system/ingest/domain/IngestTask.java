package com.sursoft.iidp.system.ingest.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sursoft.iidp.common.core.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Data @EqualsAndHashCode(callSuper = true)
@Schema(description = "接入任务")
public class IngestTask extends BaseEntity {
    private Long id;
    private String taskCode;
    private String taskName;
    private Long datasourceId;
    @Schema(description = "接入方式:REALTIME/SCHEDULED") private String ingestType;
    private String cronExpression;
    private String filterFileTypes;
    private String filterFilePattern;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") private LocalDateTime filterStartTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") private LocalDateTime filterEndTime;
    private String storageDir;
    @Schema(description = "状态:0停用1启用") private Integer status;
    // join
    private String datasourceName;
}
