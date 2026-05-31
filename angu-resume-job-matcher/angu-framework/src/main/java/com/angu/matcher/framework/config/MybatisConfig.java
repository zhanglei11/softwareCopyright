package com.angu.matcher.framework.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.angu.matcher.system.mapper")
public class MybatisConfig {}
