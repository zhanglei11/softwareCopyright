package com.imaging.scheduler.framework.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.imaging.scheduler.system.mapper")
public class MybatisConfig {}
