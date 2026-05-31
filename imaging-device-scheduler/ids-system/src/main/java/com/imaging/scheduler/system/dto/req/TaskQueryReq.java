package com.imaging.scheduler.system.dto.req;

import com.imaging.scheduler.common.core.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TaskQueryReq extends PageQuery {
    private String taskName;
    private Long sceneId;
    private Integer status;
    private Integer priority;
    private String planStartTimeBegin;
    private String planStartTimeEnd;
}
