package com.angu.matcher.system.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class InterviewResultRequest {
    @Min(1) @Max(5)
    private Integer score;
    private String comment;
    @NotBlank
    private String result;
}
