package com.angu.matcher.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "工作经历")
public class ResumeWorkExp {
    private Long id;
    private Long resumeId;
    private String company;
    private String position;
    private String industry;
    @JsonFormat(pattern = "yyyy-MM")
    private LocalDate startDate;
    @JsonFormat(pattern = "yyyy-MM")
    private LocalDate endDate;
    private String description;
}
