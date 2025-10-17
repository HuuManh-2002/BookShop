package com.example.bookshop.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays; 

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig {

    private final String[] POST_PUBLIC_ENDPOINTS = {
            "/user",
            "/auth/**"
    };
    private final String[] GET_PUBLIC_ENPOINTS = {
            "/category/**",
            "/author/**",
            "/publisher/**",
            "/payment/**",
            "/book/**",
            "/status/**",
            "/rate/**", // Đã sửa lỗi chính tả từ "rate/**"
            "/like/**", // Đã sửa lỗi chính tả từ "like/**"
            "/comment/**"
    };

    @Autowired
    CustomJwtDecoder customJwtDecoder; // Đảm bảo CustomJwtDecoder của bạn được định nghĩa đúng

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // Kích hoạt và cấu hình CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // Cấu hình quyền truy cập cho các endpoint
                .authorizeHttpRequests(request -> request
                        .requestMatchers(HttpMethod.POST, POST_PUBLIC_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.GET, GET_PUBLIC_ENPOINTS).permitAll()
                        .anyRequest().authenticated());

        httpSecurity.oauth2ResourceServer(
                oauth2 -> oauth2.jwt(jwtConfigurer -> jwtConfigurer.decoder(customJwtDecoder))
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint()));

        httpSecurity.csrf(AbstractHttpConfigurer::disable); // Tắt CSRF

        return httpSecurity.build();
    }

    // Bean này định nghĩa cấu hình CORS
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Đặt nguồn gốc mà bạn muốn cho phép
        // Đảm bảo đây là URL chính xác của ứng dụng React của bạn
        configuration.setAllowedOrigins(Arrays.asList("http://127.0.0.1:5500"));
        // Đặt các phương thức HTTP được phép, bao gồm OPTIONS cho preflight requests
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // Cho phép tất cả các header
        configuration.setAllowedHeaders(Arrays.asList("*"));
        // Cho phép gửi thông tin xác thực (cookies, authorization headers)
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Áp dụng cấu hình CORS này cho tất cả các đường dẫn
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}