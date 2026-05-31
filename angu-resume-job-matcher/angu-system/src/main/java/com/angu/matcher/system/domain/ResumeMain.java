package com.angu.matcher.system.domain;

import com.angu.matcher.common.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "简历主表")
public class ResumeMain extends BaseEntity {
    private Long id;
    private String name;
    private String phone;
    private String email;
    private Integer gender;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
    private String city;
    private String desiredPosition;
    private String desiredCity;
    private Integer desiredSalaryMin;
    private Integer desiredSalaryMax;
    private String jobStatus;
    private String highestEdu;
    private Integer totalExpYears;
    private String filePath;
    private Integer parseSuccess;
    private String source;
    private String selfIntro;
    private Integer deleted;
    private Long creatorId;
    private List<ResumeEducation> educations;
    private List<ResumeWorkExp> workExps;
    private List<ResumeSkill> skills;
}
