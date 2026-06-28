package net.oddcorner.auth_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 1. Expose BCrypt as a bean so your UserService can continue to inject it cleanly
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2. Configure the HTTP filter chains to allow public routing clearance
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF for microservice REST APIs (stateless design, no session cookies)
            .csrf(csrf -> csrf.disable())
            
            // Manage URL-based routing rules via modern lambda DSL
            .authorizeHttpRequests(auth -> auth
                // Allow anyone to access authentication routes without credentials
                .requestMatchers("/api/v1/auth/**").permitAll()
                
                // Allow access to Swagger UI & OpenAPI spec paths for automated local testing
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html", "/api-docs/**").permitAll()
                
                // Any other hidden structural endpoints require verified clearance
                .anyRequest().authenticated()
            );

        return http.build();
    }
}
