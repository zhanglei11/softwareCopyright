package com.imaging.scheduler.system.dto.req;

import com.imaging.scheduler.common.core.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DeviceQueryReq extends PageQuery {
    private Long sceneId;
    private Integer deviceType;
    private Integer status;
    private String keyword;
}
