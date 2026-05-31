package com.sursoft.vision.common.constant;

public final class Constants {
    private Constants() {}

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String LOGIN_USER_KEY = "login_user";

    public static final int USER_STATUS_ENABLE = 1;
    public static final int USER_STATUS_DISABLE = 0;

    public static final int LINE_STATUS_RUNNING = 1;
    public static final int LINE_STATUS_STOPPED = 2;
    public static final int LINE_STATUS_MAINTENANCE = 3;

    public static final int DEFECT_LEVEL_FATAL = 1;
    public static final int DEFECT_LEVEL_SERIOUS = 2;
    public static final int DEFECT_LEVEL_NORMAL = 3;

    public static final int DISPOSE_STATUS_PENDING = 0;
    public static final int DISPOSE_STATUS_PROCESSING = 1;
    public static final int DISPOSE_STATUS_DONE = 2;
    public static final int DISPOSE_STATUS_IGNORED = -1;

    public static final int ALERT_HANDLE_PENDING = 0;
    public static final int ALERT_HANDLE_DONE = 1;
    public static final int ALERT_HANDLE_IGNORED = -1;
}
