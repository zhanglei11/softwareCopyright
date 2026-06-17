package com.sursoft.iidp;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenTest {
    @Test
    void generatePassword() {
        BCryptPasswordEncoder enc = new BCryptPasswordEncoder();
        String hash = enc.encode("Admin@123");
        System.out.println("=== HASH: " + hash + " ===");
        System.out.println("=== VERIFY: " + enc.matches("Admin@123", hash) + " ===");
        System.out.println("=== OLD_MATCH: " + enc.matches("Admin@123", "$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2") + " ===");
    }
}
