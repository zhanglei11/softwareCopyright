package com.angu.ai.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.angu.ai")
@MapperScan("com.angu.ai.system.mapper")
public class AnguAiApplication {
    public static void main(String[] args) {
        SpringApplication.run(AnguAiApplication.class, args);
    }
}
