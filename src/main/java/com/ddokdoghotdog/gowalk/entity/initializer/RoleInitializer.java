package com.ddokdoghotdog.gowalk.entity.initializer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ddokdoghotdog.gowalk.auth.repository.UserRoleRepository;
import com.ddokdoghotdog.gowalk.entity.UserRole;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
@Profile("dev")
public class RoleInitializer {
    private final UserRoleRepository userRoleRepository;

    @Bean
    @Transactional
    public CommandLineRunner initalizeRoles() {

        return args -> {
            if (userRoleRepository.findByRole("ROLE_USER").isEmpty()) {
                userRoleRepository.save(UserRole.builder().role("ROLE_USER").build());
            }

            if (userRoleRepository.findByRole("ROLE_VETERINARIAN").isEmpty()) {
                userRoleRepository.save(UserRole.builder().role("ROLE_VETERINARIAN").build());
            }

            if (userRoleRepository.findByRole("ROLE_TRAINER").isEmpty()) {
                userRoleRepository.save(UserRole.builder().role("ROLE_TRAINER").build());
            }

            if (userRoleRepository.findByRole("ROLE_ADMIN").isEmpty()) {
                userRoleRepository.save(UserRole.builder().role("ROLE_ADMIN").build());
            }
        };
    }
}
