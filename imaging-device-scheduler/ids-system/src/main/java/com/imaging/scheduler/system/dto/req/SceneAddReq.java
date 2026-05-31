package com.imaging.scheduler.system.dto.req;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class SceneAddReq {
    @NotBlank @Size(max=50)
    private String sceneName;
    private Integer sceneType;
    private Long groupId;
    private Long ownerId;
    @Size(max=500)
    private String description;
    private Integer status;
}
