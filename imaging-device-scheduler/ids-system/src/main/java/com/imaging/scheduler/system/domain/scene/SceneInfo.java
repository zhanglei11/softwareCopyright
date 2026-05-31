package com.imaging.scheduler.system.domain.scene;

import com.imaging.scheduler.common.core.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SceneInfo extends BaseEntity {
    private Long id;
    private String sceneCode;
    private String sceneName;
    private Integer sceneType;
    private Long groupId;
    private Long ownerId;
    private String description;
    private Integer status;
    private Integer isDeleted;
    // 关联字段（非DB列）
    private String groupName;
}
