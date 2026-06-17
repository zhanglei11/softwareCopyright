package com.angu.ai.system.domain.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "统计查询参数")
public class StatsQuery {
    @Schema(description = "时间粒度：day / week / month")
    private String granularity = "day";
    @Schema(description = "开始日期")
    private String startDate;
    @Schema(description = "结束日期")
    private String endDate;
}
