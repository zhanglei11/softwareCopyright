package com.sursoft.vision.framework.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.sursoft.vision.system.mapper")
public class MyBatisConfig {
}
