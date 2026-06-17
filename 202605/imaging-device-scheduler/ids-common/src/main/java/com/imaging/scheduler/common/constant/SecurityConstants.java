package com.imaging.scheduler.common.constant;

public interface SecurityConstants {
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_AUTHORIZATION = "Authorization";
    long ACCESS_TOKEN_EXPIRE = 2 * 60 * 60;   // 2小时 (秒)
    long REFRESH_TOKEN_EXPIRE = 7 * 24 * 60 * 60; // 7天 (秒)
    String DEFAULT_PASSWORD = "Admin@123456";
    int MAX_LOGIN_FAIL = 5;
    long LOCK_DURATION_MINUTES = 30;
}
