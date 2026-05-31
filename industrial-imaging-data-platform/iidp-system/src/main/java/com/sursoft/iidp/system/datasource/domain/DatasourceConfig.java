package com.sursoft.iidp.system.datasource.domain;

import com.sursoft.iidp.common.core.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "数据源配置")
public class DatasourceConfig extends BaseEntity {
    private Long id;
    @Schema(description = "数据源编号") private String datasourceCode;
    @Schema(description = "数据源名称") private String datasourceName;
    @Schema(description = "类型:DEVICE/FILE_SERVER/DATABASE/OBJECT_STORAGE") private String datasourceType;
    @Schema(description = "连接地址") private String host;
    @Schema(description = "端口") private Integer port;
    @Schema(description = "认证方式:NONE/PASSWORD/KEY") private String authType;
    @Schema(description = "认证账号") private String authUsername;
    @Schema(description = "认证密码(脱敏)") private String authPassword;
    @Schema(description = "密钥(脱敏)") private String authKey;
    @Schema(description = "数据格式:JPEG/PNG/RAW/MP4/OTHER") private String dataFormat;
    @Schema(description = "扩展配置JSON") private String extConfig;
    @Schema(description = "负责人用户ID") private Long ownerId;
    @Schema(description = "状态:0停用1启用") private Integer status;
    @Schema(description = "备注") private String remark;
}
