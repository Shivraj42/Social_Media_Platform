package com.example.social_media_plateform.Config;

import com.example.social_media_plateform.Enums.Role;
import com.example.social_media_plateform.Models.User;
import com.example.social_media_plateform.Repositories.UserRepository;
import com.example.social_media_plateform.Services.Impls.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SeedDataConfig implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @Override
    public void run(String... args) throws Exception {

        if (userRepository.count() == 0) {

            User admin = User
                    .builder()
                    .firstName("admin")
                    .lastName("admin")
                    .username("admin")
                    .email("admin@admin.com")
                    .password(passwordEncoder.encode("password"))
                    .role(Role.ROLE_ADMIN)
                    .verified(true)
                    .build();

            userService.save(admin);
            log.debug("created ADMIN user - {}", admin);
        }
    }
}
