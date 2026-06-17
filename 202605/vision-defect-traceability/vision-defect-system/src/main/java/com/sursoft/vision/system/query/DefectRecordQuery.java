package com.sursoft.vision.system.query;

import lombok.Data;

@Data
public class DefectRecordQuery {
    private Long lineId;
    private Long productId;
    private Long categoryId;
    private Integer level;
    private Integer result;
    private String shift;
    private String startTime;
    private String endTime;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}
