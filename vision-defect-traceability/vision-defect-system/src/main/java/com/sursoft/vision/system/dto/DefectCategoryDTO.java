package com.sursoft.vision.system.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class DefectCategoryDTO {
    @NotBlank @Pattern(regexp="DEF-\\w+", message="编码格式应为 DEF-XXX")
    private String code;
    @NotBlank @Size(min=2, max=50)
    private String name;
    @NotNull @Min(1) @Max(3)
    private Integer level;
    @Size(max=500)
    private String description;
    @NotNull
    private Integer status;
}
