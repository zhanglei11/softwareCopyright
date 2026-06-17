package com.sursoft.vision.system.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class LineDTO {
    @NotBlank @Size(max=50)
    private String lineNo;
    @NotBlank @Size(min=2, max=50)
    private String lineName;
    @NotBlank @Size(min=2, max=50)
    private String workshop;
    @NotNull
    private Long managerId;
    @NotNull
    private Integer status;
    @Size(max=500)
    private String remark;
}
