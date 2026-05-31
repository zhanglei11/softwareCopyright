package com.imaging.scheduler.system.dto.req;

import com.imaging.scheduler.common.core.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "调度日志查询请求")
public class DispatchLogQueryReq extends PageQuery {
    @Schema(description = "任务ID")
    private Long taskId;
    @Schema(description = "操作动作")
    private String action;
    @Schema(description = "操作人ID")
    private Long operatorId;
}
