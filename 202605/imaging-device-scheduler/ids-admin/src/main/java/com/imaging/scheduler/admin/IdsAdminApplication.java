package com.imaging.scheduler.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.imaging.scheduler")
public class IdsAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(IdsAdminApplication.class, args);
    }
}
