package com.bloodnetwork.config;

import com.bloodnetwork.entity.Role;
import com.bloodnetwork.entity.User;
import com.bloodnetwork.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.findByEmail("admin@bloodnetwork.com").isEmpty()) {
            User admin = User.builder()
                    .name("System Admin")
                    .email("admin@bloodnetwork.com")
                    .password(passwordEncoder.encode("admin123"))
                    .phone("0000000000")
                    .role(Role.ADMIN)
                    .build();
            
            userRepository.save(admin);
            log.info("Default Admin account created successfully!");
            log.info("Email: admin@bloodnetwork.com");
            log.info("Password: admin123");
        }
    }
}
