package com.imaging.scheduler.common.enums;

import lombok.Getter;

@Getter
public enum TaskStatus {
    PENDING(10, "待分配"),
    ASSIGNED(20, "已分配"),
    RUNNING(30, "执行中"),
    COMPLETED(50, "已完成"),
    CANCELLED(-20, "已取消");

    private final int code;
    private final String label;

    TaskStatus(int code, String label) {
        this.code = code;
        this.label = label;
    }
}
