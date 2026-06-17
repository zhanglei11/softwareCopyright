package com.angu.matcher.system.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ApplicationStatusRequest {
    @NotBlank(message = "目标状态不能为空")
    private String status;
    private String remark;
}
