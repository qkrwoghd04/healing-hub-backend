package com.healing_hub.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 공격 방어 기능 비활성화 (개발 편의성)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/products").permitAll() // "/", "/products"는 누구나 접근 가능
                        .anyRequest().authenticated() // 나머지 모든 요청은 인증 필요
                );
        return http.build();
    }
}
