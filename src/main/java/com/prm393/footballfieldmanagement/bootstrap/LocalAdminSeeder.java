package com.prm393.footballfieldmanagement.bootstrap;

import com.prm393.footballfieldmanagement.entity.User;
import com.prm393.footballfieldmanagement.enums.Role;
import com.prm393.footballfieldmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile("local")
@RequiredArgsConstructor
public class LocalAdminSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${local-admin.email}")
    private String adminEmail;

    @Value("${local-admin.password}")
    private String adminPassword;

    @Override
    public void run(String... args) {
        if (userRepository.existsByEmailIgnoreCase(adminEmail)) {
            return;
        }

        User admin = User.builder()
                .fullName("Administrator")
                .email(adminEmail)
                .phone(null)
                .passwordHash(passwordEncoder.encode(adminPassword))
                .role(Role.ADMIN)
                .avatarUrl(null)
                .isActive(true)
                .build();
        userRepository.save(admin);
    }
}
