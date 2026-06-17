package com.sursoft.vision.system.vo;

import lombok.Data;
import java.util.List;

@Data
public class BatchTraceVO {
    private String batchNo;
    private String lineName;
    private Long totalCount;
    private Long qualifiedCount;
    private Long defectCount;
    private Double qualifiedRate;
    private List<CategoryDistVO> categoryDistribution;
    private List<DefectRecordVO> records;

    @Data
    public static class CategoryDistVO {
        private String categoryName;
        private Long count;
        private Double ratio;
    }
}
