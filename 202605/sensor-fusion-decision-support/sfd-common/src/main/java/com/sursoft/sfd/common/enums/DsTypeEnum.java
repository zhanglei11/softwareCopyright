package com.sursoft.sfd.common.enums;

public enum DsTypeEnum {
    DEVICE("设备直连"),
    FILE_SERVER("文件服务器"),
    DATABASE("数据库"),
    OBJECT_STORAGE("对象存储");

    private final String label;
    DsTypeEnum(String label) { this.label = label; }
    public String getLabel() { return label; }
}
