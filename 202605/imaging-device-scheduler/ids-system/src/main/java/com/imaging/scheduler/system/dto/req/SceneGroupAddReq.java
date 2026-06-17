package com.imaging.scheduler.system.dto.req;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class SceneGroupAddReq {
    @NotBlank @Size(max=30)
    private String groupName;
    @Size(max=200)
    private String remark;
}
