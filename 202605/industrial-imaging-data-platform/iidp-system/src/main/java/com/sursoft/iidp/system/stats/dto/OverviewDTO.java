package com.sursoft.iidp.system.stats.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "平台概览统计")
public class OverviewDTO {
    private Long datasourceCount;
    private Long activeDatasourceCount;
    private Long todayIngestCount;
    private Long todayIngestSize;
    private Long todayProcessCount;
    private Double storageUsageRate;
    private Long ingestTaskCount;
    private Long processTaskCount;
}
