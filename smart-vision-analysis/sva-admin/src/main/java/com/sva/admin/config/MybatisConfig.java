package com.sva.admin.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan({"com.sva.system.mapper", "com.sva.framework.security.mapper"})
public class MybatisConfig {
}
