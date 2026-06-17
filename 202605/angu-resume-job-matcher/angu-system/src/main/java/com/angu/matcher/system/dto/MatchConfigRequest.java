package com.angu.matcher.system.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MatchConfigRequest {
    @NotNull
    private Integer skillWeight;
    @NotNull
    private Integer eduWeight;
    @NotNull
    private Integer expWeight;
}
