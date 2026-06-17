package com.angu.matcher.system.domain;

import com.angu.matcher.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "职位信息")
public class JobPosition extends BaseEntity {
    private Long id;
    private String title;
    private String department;
    private String jobType;
    private String location;
    private Integer salaryMin;
    private Integer salaryMax;
    private String eduRequire;
    private Integer expRequire;
    private String description;
    private String skillTags;
    private String status;
    private Integer deleted;
    private Long creatorId;
}
