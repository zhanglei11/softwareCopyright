package com.sursoft.vision.system.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DefectDisposeDTO {
    @NotNull
    private Integer disposeStatus;
    private String disposeRemark;
}
