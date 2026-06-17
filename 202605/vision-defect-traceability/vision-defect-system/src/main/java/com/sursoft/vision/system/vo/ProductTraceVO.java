package com.sursoft.vision.system.vo;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProductTraceVO {
    private String serialNo;
    private String typeName;
    private String batchNo;
    private String lineName;
    private List<DetectRecordItem> detectRecords;
    private String finalConclusion;

    @Data
    public static class DetectRecordItem {
        private Long id;
        private LocalDateTime detectTime;
        private Integer result;
        private String resultLabel;
        private String categoryName;
        private Integer level;
        private Integer disposeStatus;
        private String disposeRemark;
    }
}
