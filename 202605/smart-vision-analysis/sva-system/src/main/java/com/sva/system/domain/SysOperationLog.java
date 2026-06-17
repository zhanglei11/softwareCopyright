package com.sva.system.domain;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SysOperationLog {
    private Long id;
    private Long userId;
    private String username;
    private String module;
    private String operation;
    private String requestMethod;
    private String requestUrl;
    private String requestParams;
    private String responseResult;
    private Integer status;
    private String ip;
    private LocalDateTime createdAt;
}
