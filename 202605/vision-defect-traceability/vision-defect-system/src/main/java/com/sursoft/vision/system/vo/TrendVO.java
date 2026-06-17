package com.sursoft.vision.system.vo;

import lombok.Data;
import java.util.List;

@Data
public class TrendVO {
    private Long totalCount;
    private Double avgQualifiedRate;
    private List<TrendItem> series;

    @Data
    public static class TrendItem {
        private String date;
        private Long totalCount;
        private Double qualifiedRate;
    }
}
