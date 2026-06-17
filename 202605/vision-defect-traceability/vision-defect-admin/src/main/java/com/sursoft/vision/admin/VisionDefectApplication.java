package com.sursoft.vision.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.sursoft.vision")
public class VisionDefectApplication {
    public static void main(String[] args) {
        SpringApplication.run(VisionDefectApplication.class, args);
    }
}
