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
        log.info("Checking Admin account seeding status...");
        
        if (!adminSeedEnabled) {
            log.info("Admin seed is DISABLED. Set ADMIN_SEED_ENABLED=true if you want to create the default admin.");
            return;
        }

        if (!StringUtils.hasText(adminPassword)) {
            log.error("ADMIN_PASSWORD is empty! Cannot seed admin account.");
            throw new IllegalStateException("ADMIN_PASSWORD is required when ADMIN_SEED_ENABLED=true");
        }

        userRepository.findByEmailIgnoreCase(adminEmail).ifPresentOrElse(
            existingUser -> {
                log.info("User with email {} already exists.", adminEmail);
                if (existingUser.getRole() != Role.ADMIN) {
                    log.warn("Existing user {} has role {}, upgrading to ADMIN...", adminEmail, existingUser.getRole());
                    existingUser.setRole(Role.ADMIN);
                    userRepository.save(existingUser);
                    log.info("Admin role granted to {}", adminEmail);
                } else {
                    log.info("Admin account {} is already configured correctly.", adminEmail);
                }
            },
            () -> {
                log.info("Creating default Admin account for email: {}", adminEmail);
                User admin = User.builder()
                        .name(adminName)
                        .email(adminEmail)
                        .password(passwordEncoder.encode(adminPassword))
                        .phone(adminPhone)
                        .role(Role.ADMIN)
                        .build();
                
                userRepository.save(admin);
                log.info("Default Admin account created successfully!");
            }
        );
    }
}
