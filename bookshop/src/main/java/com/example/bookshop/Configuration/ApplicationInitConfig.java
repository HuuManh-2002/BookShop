package com.example.bookshop.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.bookshop.Entity.Role;
import com.example.bookshop.Entity.User;
import com.example.bookshop.Entity.UserRole;
import com.example.bookshop.Repository.RoleRepository;
import com.example.bookshop.Repository.UserRepository;
import com.example.bookshop.Repository.UserRoleRepository;

@Configuration
public class ApplicationInitConfig {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserRoleRepository userroleRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner() {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
                User user = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("manh2002"))
                        .firstName("Huu")
                        .lastName("Manh")
                        .created_Time(LocalDateTime.now())
                        .dob(LocalDate.of(2002, 6, 10))
                        .email("admin@gmail.com")
                        .phoneNumber("0862083358")
                        .gender("Nam")
                        .build();

                userRepository.save(user);
                Role role = roleRepository.findById(Long.valueOf(1)).get();

                UserRole user_role = UserRole.builder()
                        .user(user)
                        .role(role)
                        .createdTime(LocalDateTime.now())
                        .build();

                userroleRepository.save(user_role);
            }
        };
    }
}
