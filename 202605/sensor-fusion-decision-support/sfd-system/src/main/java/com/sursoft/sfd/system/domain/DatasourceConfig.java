package com.sursoft.sfd.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sursoft.sfd.common.core.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter @Setter
@Schema(description = "数据源配置")
public class DatasourceConfig extends BaseEntity {
    @Schema(description = "数据源编号") private String dsCode;
    @Schema(description = "数据源名称") private String dsName;
    @Schema(description = "所属场景")   private String sceneType;
    @Schema(description = "数据源类型") private String dsType;
    @Schema(description = "连接地址")   private String connHost;
    @Schema(description = "端口")       private Integer connPort;
    @Schema(description = "认证方式")   private String authType;
    @Schema(description = "认证配置（加密）") private String authConfig;
    @Schema(description = "字段映射配置") private String fieldMapping;
    // 关联状态（非持久化字段，查询时填充）
    @Schema(description = "连接状态 0异常 1正常") private Integer connStatus;
    @Schema(description = "最近数据时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastDataTime;
}
