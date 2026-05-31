package com.sursoft.vision.system.query;

import lombok.Data;

@Data
public class AlertRecordQuery {
    private Long ruleId;
    private Integer handleStatus;
    private String startTime;
    private String endTime;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}
