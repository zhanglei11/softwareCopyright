package com.angu.matcher.system.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ApplicationCreateRequest {
    @NotNull(message = "职位ID不能为空")
    private Long positionId;
    @NotNull(message = "简历ID不能为空")
    private Long resumeId;
    private String remark;
}
