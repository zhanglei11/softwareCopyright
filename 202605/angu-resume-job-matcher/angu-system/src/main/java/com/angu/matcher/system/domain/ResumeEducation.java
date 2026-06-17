package com.angu.matcher.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "教育经历")
public class ResumeEducation {
    private Long id;
    private Long resumeId;
    private String school;
    private String major;
    private String eduLevel;
    @JsonFormat(pattern = "yyyy-MM")
    private LocalDate startDate;
    @JsonFormat(pattern = "yyyy-MM")
    private LocalDate endDate;
}
