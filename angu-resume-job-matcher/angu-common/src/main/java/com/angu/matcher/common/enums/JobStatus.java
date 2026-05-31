package com.angu.matcher.common.enums;

public enum JobStatus {
    DRAFT("草稿"),
    OPEN("发布中"),
    CLOSED("已关闭");

    private final String desc;
    JobStatus(String desc) { this.desc = desc; }
    public String getDesc() { return desc; }
}
