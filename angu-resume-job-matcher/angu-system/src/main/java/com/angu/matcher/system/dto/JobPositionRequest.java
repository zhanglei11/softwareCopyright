package com.angu.matcher.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "职位创建/编辑请求")
public class JobPositionRequest {
    @NotBlank(message = "职位名称不能为空")
    private String title;
    private String department;
    private String jobType;
    private String location;
    private Integer salaryMin;
    private Integer salaryMax;
    private String eduRequire;
    private Integer expRequire;
    private String description;
    private List<String> skillTags;
}
