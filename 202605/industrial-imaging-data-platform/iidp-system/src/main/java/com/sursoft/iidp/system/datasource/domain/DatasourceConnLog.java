package com.sursoft.iidp.system.datasource.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "数据源连接测试日志")
public class DatasourceConnLog {
    private Long id;
    private Long datasourceId;
    @Schema(description = "检测结果:0失败1成功") private Integer result;
    private String errorMsg;
    @Schema(description = "耗时ms") private Integer costTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime testedAt;
    private Long testedBy;
}
