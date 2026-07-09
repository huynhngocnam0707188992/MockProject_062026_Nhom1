package com.eldercare.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> {}) // Leave it to custom CorsConfig or disable if not needed
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Bypass auth for facilities API for testing
                .requestMatchers("/api/v1/admin/facility-settings/**").permitAll()
                // All other requests also permitted for now during early development
                .anyRequest().permitAll()
            )
            .formLogin(AbstractHttpConfigurer::disable) // Disable default login form
            .httpBasic(AbstractHttpConfigurer::disable);

        return http.build();
    }
}
