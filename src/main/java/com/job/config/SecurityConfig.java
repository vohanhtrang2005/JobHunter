package com.job.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // tắt CSRF để POST/PUT/DELETE chạy thoải mái
            .authorizeHttpRequests()
            .anyRequest().permitAll(); // tất cả request (GET, POST, ...) đều cho phép

        return http.build();
    }
}
