package com.sva.system.query;
import lombok.Data;
@Data
public class TaskQuery {
    private String taskName;
    private Integer status;
    private Long modelVersionId;
    private String startTime;
    private String endTime;
}
