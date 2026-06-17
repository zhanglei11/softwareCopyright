package com.angu.matcher.common.enums;

public enum EduLevel {
    HIGH_SCHOOL(1, "高中"),
    ASSOCIATE(2, "大专"),
    BACHELOR(3, "本科"),
    MASTER(4, "硕士"),
    DOCTOR(5, "博士");

    private final int level;
    private final String desc;

    EduLevel(int level, String desc) {
        this.level = level;
        this.desc = desc;
    }

    public int getLevel() { return level; }
    public String getDesc() { return desc; }
}
