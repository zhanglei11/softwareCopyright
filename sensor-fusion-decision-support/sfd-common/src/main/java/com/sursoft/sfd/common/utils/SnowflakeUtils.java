package com.sursoft.sfd.common.utils;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

public final class SnowflakeUtils {

    private static final Snowflake SNOWFLAKE = IdUtil.getSnowflake(1, 1);

    private SnowflakeUtils() {}

    public static long nextId() {
        return SNOWFLAKE.nextId();
    }
}
