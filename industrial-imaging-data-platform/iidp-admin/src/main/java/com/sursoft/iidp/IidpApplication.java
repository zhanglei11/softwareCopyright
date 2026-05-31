package com.sursoft.iidp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.sursoft.iidp.system.**.mapper")
public class IidpApplication {
    public static void main(String[] args) {
        SpringApplication.run(IidpApplication.class, args);
    }
}
