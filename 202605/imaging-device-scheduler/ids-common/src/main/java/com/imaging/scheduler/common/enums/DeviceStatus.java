package com.imaging.scheduler.common.enums;

import lombok.Getter;

@Getter
public enum DeviceStatus {
    ONLINE(1, "在线"),
    BUSY(2, "占用中"),
    OFFLINE(3, "离线"),
    FAULT(4, "故障"),
    MAINTENANCE(5, "维护中");

    private final int code;
    private final String label;

    DeviceStatus(int code, String label) {
        this.code = code;
        this.label = label;
    }
}
