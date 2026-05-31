package com.sva.system.query;
import lombok.Data;
@Data
public class LogQuery {
    private String username;
    private String module;
    private Integer status;
    private String startTime;
    private String endTime;
}
