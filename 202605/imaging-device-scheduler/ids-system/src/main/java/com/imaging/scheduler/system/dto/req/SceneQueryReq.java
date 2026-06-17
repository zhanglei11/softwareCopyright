package com.imaging.scheduler.system.dto.req;

import com.imaging.scheduler.common.core.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SceneQueryReq extends PageQuery {
    private String sceneName;
    private Integer sceneType;
    private Integer status;
    private Long groupId;
}
