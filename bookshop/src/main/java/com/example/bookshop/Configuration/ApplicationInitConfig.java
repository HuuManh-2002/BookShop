package com.example.bookshop.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays; // Import thêm để dùng Arrays.asList
import java.util.List; // Import List

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.bookshop.Entity.Payment;
import com.example.bookshop.Entity.PaymentMethodEnum;
import com.example.bookshop.Entity.Role;
import com.example.bookshop.Entity.Status;
import com.example.bookshop.Entity.StatusOrderEnum;
import com.example.bookshop.Entity.User;
import com.example.bookshop.Entity.UserRole;
import com.example.bookshop.Repository.PaymentRepository;
import com.example.bookshop.Repository.RoleRepository;
import com.example.bookshop.Repository.StatusRepository;
import com.example.bookshop.Repository.UserRepository;
import com.example.bookshop.Repository.UserRoleRepository;

@Configuration
public class ApplicationInitConfig {

        // --- KHAI BÁO REPOSITORY BẰNG FIELD INJECTION ---
        @Autowired
        UserRepository userRepository;
        @Autowired
        UserRoleRepository userroleRepository;
        @Autowired
        StatusRepository statusRepository;
        @Autowired
        PaymentRepository paymentRepository;
        @Autowired
        RoleRepository roleRepository;
        @Autowired
        PasswordEncoder passwordEncoder;

        // --- ĐÃ XÓA KHAI BÁO: StatusOrderEnum statusOrderEnum; và PaymentMethodEnum
        // paymentMethodEnum;
        // Bạn KHÔNG được khai báo và tiêm (@Autowired) các Enum.

        @Bean
        ApplicationRunner applicationRunner() {
                return args -> {

                        // ===============================================
                        // 1. KHỞI TẠO USER & ROLE (Không thay đổi)
                        // ===============================================
                        if (userRepository.findByEmail("admin@gmail.com").isEmpty()) {
                                // ... (Logic tạo User và Role)
                                Role adminRole = Role.builder().name("ADMIN").description("Quyền quản trị viên")
                                                .build();
                                Role userRole = Role.builder().name("USER").description("Quyền người dùng").build();
                                roleRepository.save(adminRole);
                                roleRepository.save(userRole);

                                User user = User.builder()
                                                .email("admin@gmail.com")
                                                .password(passwordEncoder.encode("manh2002"))
                                                .firstName("Huu").lastName("Manh")
                                                .created_Time(LocalDateTime.now()).dob(LocalDate.of(2002, 6, 10))
                                                .phoneNumber("0862083358").gender("Nam")
                                                .build();
                                userRepository.save(user);

                                Role roleAdmin = roleRepository.findById(Long.valueOf(1)).get();
                                Role roleUser = roleRepository.findById(Long.valueOf(2)).get();

                                UserRole user_role_Admin = UserRole.builder().user(user).role(roleAdmin)
                                                .createdTime(LocalDateTime.now()).build();
                                UserRole user_role_User = UserRole.builder().user(user).role(roleUser)
                                                .createdTime(LocalDateTime.now()).build();
                                userroleRepository.save(user_role_Admin);
                                userroleRepository.save(user_role_User);
                        }

                        // ===============================================
                        // 2. KHỞI TẠO TRẠNG THÁI ĐƠN HÀNG (Sử dụng saveAll để tối ưu)
                        // ===============================================
                        if (statusRepository.count() == 0) {

                                // Thu thập tất cả Status Entity cần lưu
                                List<Status> initialStatuses = Arrays.stream(StatusOrderEnum.values())
                                                .map(statusEnum -> Status.builder()
                                                                // .id(statusEnum.getId())
                                                                .name(statusEnum.getName())
                                                                .description(statusEnum.getDescription())
                                                                .build())
                                                .toList(); // Hoặc .collect(Collectors.toList());

                                // Sử dụng saveAll() để tối ưu hóa việc lưu vào DB
                                statusRepository.saveAll(initialStatuses);
                        }

                        // ===============================================
                        // 3. KHỞI TẠO PHƯƠNG THỨC THANH TOÁN (Sử dụng saveAll để tối ưu)
                        // ===============================================
                        if (paymentRepository.count() == 0) {
                                // Thu thập tất cả Payment Entity cần lưu
                                List<Payment> initialPayments = Arrays.stream(PaymentMethodEnum.values())
                                                .map(paymentEnum -> Payment.builder()
                                                                // .id(paymentEnum.getId())
                                                                .name(paymentEnum.getName())
                                                                .description(paymentEnum.getDescription())
                                                                .build())
                                                .toList(); // Hoặc .collect(Collectors.toList());

                                // Sử dụng saveAll() để tối ưu hóa việc lưu vào DB
                                paymentRepository.saveAll(initialPayments);
                        }
                };
        }
}