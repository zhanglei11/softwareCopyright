package com.angu.matcher.system.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InterviewRequest {
    @NotNull
    private Long applicationId;
    private LocalDateTime interviewTime;
    private String interviewer;
    private String location;
}
