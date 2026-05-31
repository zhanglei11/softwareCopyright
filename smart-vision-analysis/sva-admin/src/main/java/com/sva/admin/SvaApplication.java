package com.sva.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.sva")
public class SvaApplication {
    public static void main(String[] args) {
        SpringApplication.run(SvaApplication.class, args);
    }
}
