package com.sva.system.vo;
import lombok.Data;
@Data
public class TaskProgressVO {
    private Long taskId;
    private Integer status;
    private Integer totalCount;
    private Integer processedCount;
    private Integer successCount;
    private Integer failCount;
    private Double progress;
}
