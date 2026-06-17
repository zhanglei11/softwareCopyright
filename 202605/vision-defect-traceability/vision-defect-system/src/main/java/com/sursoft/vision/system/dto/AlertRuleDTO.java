package com.sursoft.vision.system.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class AlertRuleDTO {
    @NotBlank @Size(min=2, max=100)
    private String ruleName;
    private Long lineId;
    @NotNull @Min(1) @Max(2)
    private Integer conditionType;
    @NotNull
    private BigDecimal threshold;
    @NotNull @Min(1) @Max(3)
    private Integer statCycle;
    @NotNull @Min(1) @Max(3)
    private Integer alertLevel;
    private List<Long> notifyUserIds;
    @NotNull
    private Integer status;
}
