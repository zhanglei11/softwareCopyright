package com.angu.matcher.common.constant;

public final class Constants {
    private Constants() {}
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_AUTH = "Authorization";
    public static final int TOKEN_EXPIRE_MINUTES = 120;
    public static final int REFRESH_TOKEN_EXPIRE_DAYS = 7;
    public static final int LOGIN_MAX_ERROR = 5;
    public static final int LOCK_MINUTES = 30;
    public static final long MB_10 = 10 * 1024 * 1024L;
}
