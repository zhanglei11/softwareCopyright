package com.sursoft.sfd.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter @Setter
@Schema(description = "数据源状态记录")
public class DatasourceStatus {
    @Schema(description = "记录ID") private Long id;
    @Schema(description = "数据源ID") private Long dsId;
    @Schema(description = "连接状态 0异常 1正常") private Integer connStatus;
    @Schema(description = "异常描述") private String errorMsg;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "最近数据时间") private LocalDateTime lastDataTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "检测时间") private LocalDateTime checkedAt;
}
