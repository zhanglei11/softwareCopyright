package com.sursoft.vision.system.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ProductTypeDTO {
    @NotBlank @Size(max=50)
    private String typeNo;
    @NotBlank @Size(min=2, max=50)
    private String typeName;
    @NotNull
    private Long lineId;
    @NotNull
    private Integer status;
}
