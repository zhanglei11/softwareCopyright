package com.sursoft.sfd;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.sursoft.sfd.system.mapper")
public class SfdApplication {
    public static void main(String[] args) {
        SpringApplication.run(SfdApplication.class, args);
    }
}
