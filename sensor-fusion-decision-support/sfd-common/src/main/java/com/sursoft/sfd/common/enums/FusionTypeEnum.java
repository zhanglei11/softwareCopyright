package com.sursoft.sfd.common.enums;

public enum FusionTypeEnum {
    WEIGHTED("加权融合"),
    VOTE("投票融合"),
    PRIORITY("优先级融合");

    private final String label;
    FusionTypeEnum(String label) { this.label = label; }
    public String getLabel() { return label; }
}
