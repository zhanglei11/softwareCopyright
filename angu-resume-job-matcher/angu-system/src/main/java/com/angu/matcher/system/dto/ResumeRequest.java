package com.angu.matcher.system.dto;

import com.angu.matcher.system.domain.ResumeEducation;
import com.angu.matcher.system.domain.ResumeWorkExp;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Schema(description = "简历创建/编辑请求")
public class ResumeRequest {
    private String name;
    private String phone;
    private String email;
    private Integer gender;
    private LocalDate birthDate;
    private String city;
    private String desiredPosition;
    private String desiredCity;
    private Integer desiredSalaryMin;
    private Integer desiredSalaryMax;
    private String jobStatus;
    private String selfIntro;
    private String source;
    private List<ResumeEducation> educations;
    private List<ResumeWorkExp> workExps;
    private List<String> skills;
}
