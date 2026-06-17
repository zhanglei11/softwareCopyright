package com.imaging.scheduler.system.domain.scene;

import com.imaging.scheduler.common.core.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SceneGroup extends BaseEntity {
    private Long id;
    private String groupCode;
    private String groupName;
    private String remark;
    private Integer isDeleted;
}
