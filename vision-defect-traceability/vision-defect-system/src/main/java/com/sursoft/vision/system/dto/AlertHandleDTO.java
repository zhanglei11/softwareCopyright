package com.sursoft.vision.system.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AlertHandleDTO {
    @NotNull
    private Integer handleStatus;
    private String handleRemark;
}
