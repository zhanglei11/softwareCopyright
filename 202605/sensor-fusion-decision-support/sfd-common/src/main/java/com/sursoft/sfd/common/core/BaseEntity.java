package com.sursoft.sfd.common.core;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class BaseEntity implements Serializable {

    @Schema(description = "主键ID（雪花）")
    private Long id;

    @Schema(description = "状态：0-停用 1-启用")
    private Integer status;

    @Schema(description = "逻辑删除：0-正常 1-已删除")
    private Integer isDeleted;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @Schema(description = "创建人ID")
    private Long createdBy;

    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    @Schema(description = "更新人ID")
    private Long updatedBy;

    @Schema(description = "备注")
    private String remark;
}
