package com.angu.matcher.system.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "技能标签")
public class ResumeSkill {
    private Long id;
    private Long resumeId;
    private String skillName;
}
