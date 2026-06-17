package com.vqcc.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.vqcc"})
@MapperScan("com.vqcc.system.mapper")
public class VqccApplication {
    public static void main(String[] args) {
        SpringApplication.run(VqccApplication.class, args);
    }
}
