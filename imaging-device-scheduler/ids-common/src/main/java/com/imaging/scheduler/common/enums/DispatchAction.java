package com.imaging.scheduler.common.enums;

import lombok.Getter;

@Getter
public enum DispatchAction {
    CREATE(1, "创建任务"),
    ASSIGN(2, "分配设备"),
    UNASSIGN(3, "撤销分配"),
    START(4, "启动任务"),
    COMPLETE(5, "完成任务"),
    CANCEL(6, "取消任务"),
    FAULT(7, "设备故障");

    private final int code;
    private final String label;

    DispatchAction(int code, String label) {
        this.code = code;
        this.label = label;
    }
}
