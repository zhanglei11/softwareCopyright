package com.angu.ai.system.domain.entity;

import com.angu.ai.common.core.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "用户场景收藏")
public class UserSceneFavorite extends BaseEntity {

    @Schema(description = "用户 ID")
    private Long userId;

    @Schema(description = "场景 ID")
    private Long sceneId;
}
