package com.sursoft.iidp.system.storage.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "数据清理日志")
public class StorageCleanLog {
    private Long id;
    private Long ruleId;
    private String ruleName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") private LocalDateTime executeTime;
    private String executeType;
    private Integer deletedCount;
    private Long freedBytes;
    private String executeStatus;
    private String errorMsg;
    private Long executedBy;
}
