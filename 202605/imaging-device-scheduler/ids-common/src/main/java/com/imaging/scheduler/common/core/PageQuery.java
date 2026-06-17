package com.imaging.scheduler.common.core;

import lombok.Data;

/**
 * 分页查询基类
 */
@Data
public class PageQuery {
    private int page = 1;
    private int pageSize = 10;
}
