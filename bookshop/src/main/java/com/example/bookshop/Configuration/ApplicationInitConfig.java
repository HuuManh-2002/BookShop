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
            if (userRepository.findByEmail("admin@gmail.com").isEmpty()) {
                Role adminRole = Role.builder()
                        .name("ADMIN")
                        .description("Quyền quản trị viên")
                        .build();
                Role userRole = Role.builder()
                        .name("USER")
                        .description("Quyền người dùng")
                        .build();
                roleRepository.save(adminRole);
                roleRepository.save(userRole);
                User user = User.builder()
                        .email("admin@gmail.com")
                        .password(passwordEncoder.encode("manh2002"))
                        .firstName("Huu")
                        .lastName("Manh")
                        .created_Time(LocalDateTime.now())
                        .dob(LocalDate.of(2002, 6, 10))
                        .phoneNumber("0862083358")
                        .gender("Nam")
                        .build();


                userRepository.save(user);
                Role roleAdmin = roleRepository.findById(Long.valueOf(1)).get();
                Role roleUser = roleRepository.findById(Long.valueOf(2)).get();

                UserRole user_role_Admin = UserRole.builder()
                        .user(user)
                        .role(roleAdmin)
                        .createdTime(LocalDateTime.now())
                        .build();
                UserRole user_role_User = UserRole.builder()
                        .user(user)
                        .role(roleUser)
                        .createdTime(LocalDateTime.now())
                        .build();
                userroleRepository.save(user_role_Admin);
                userroleRepository.save(user_role_User);
            }
        };
    }
}
