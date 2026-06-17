package com.angu.matcher.common.enums;

public enum ApplicationStatus {
    PENDING("待筛选"),
    RESUME_PASSED("简历通过"),
    RESUME_REJECTED("简历淘汰"),
    INTERVIEW_WAITING("待面试"),
    INTERVIEWING("面试中"),
    INTERVIEW_PASSED("面试通过"),
    INTERVIEW_REJECTED("面试淘汰"),
    HIRED("已录用");

    private final String desc;
    ApplicationStatus(String desc) { this.desc = desc; }
    public String getDesc() { return desc; }
}
