package com.sursoft.sfd.common.enums;

public enum SceneTypeEnum {
    QUALITY("质检产线"),
    STORAGE("仓储"),
    LOGISTICS("物流"),
    SECURITY("安防");

    private final String label;
    SceneTypeEnum(String label) { this.label = label; }
    public String getLabel() { return label; }
}
