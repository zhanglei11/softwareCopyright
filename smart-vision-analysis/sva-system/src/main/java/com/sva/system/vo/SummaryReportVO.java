package com.sva.system.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class SummaryReportVO {
    private Integer totalTasks;
    private Integer totalImages;
    private BigDecimal successRate;
    private BigDecimal avgConfidence;
    private List<TrendItem> trend;
    private Map<String, Integer> modelDistribution;
    private List<TaskSummaryItem> taskSummaries;

    @Data
    public static class TrendItem {
        private String date;
        private Integer count;
    }

    @Data
    public static class TaskSummaryItem {
        private Long taskId;
        private String taskName;
        private String modelName;
        private Integer totalImages;
        private BigDecimal successRate;
        private BigDecimal avgConfidence;
        private String finishTime;
    }
}
