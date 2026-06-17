package com.sursoft.vision.system.vo;

import lombok.Data;
import java.util.List;

@Data
public class DashboardVO {
    private Long todayTotal;
    private Double todayQualifiedRate;
    private Long todayDefectCount;
    private List<TrendItem> monthTrend;
    private List<NameValueVO> categoryDistribution;
    private List<LineCompareVO> lineComparison;

    @Data
    public static class TrendItem {
        private String date;
        private Double qualifiedRate;
    }

    @Data
    public static class LineCompareVO {
        private String lineName;
        private Long defectCount;
    }
}
