package com.bloodnetwork.config;

import com.bloodnetwork.entity.Role;
import com.bloodnetwork.entity.User;
import com.bloodnetwork.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.seed.enabled:false}")
    private boolean adminSeedEnabled;

    @Value("${app.admin.email:admin@bloodnetwork.com}")
    private String adminEmail;

    @Value("${app.admin.password:}")
    private String adminPassword;

    @Value("${app.admin.name:System Admin}")
    private String adminName;

    @Value("${app.admin.phone:0000000000}")
    private String adminPhone;

    @Override
    public void run(String... args) {
        if (!adminSeedEnabled) {
            log.info("Admin seed is disabled.");
            return;
        }

        if (!StringUtils.hasText(adminPassword)) {
            throw new IllegalStateException("ADMIN_PASSWORD is required when ADMIN_SEED_ENABLED=true");
        }

        if (userRepository.findByEmail(adminEmail).isEmpty()) {
            User admin = User.builder()
                    .name(adminName)
                    .email(adminEmail)
                    .password(passwordEncoder.encode(adminPassword))
                    .phone(adminPhone)
                    .role(Role.ADMIN)
                    .build();
            
            userRepository.save(admin);
            log.info("Default Admin account created successfully!");
            log.info("Email: {}", adminEmail);
        }
    }
}
