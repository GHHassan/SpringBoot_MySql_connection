package com.example.sqlconnect.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class WebSecurity {

//    define Security
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
            .csrf(csrf -> csrf // Enable CSRF protection
                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) // Use cookie-based CSRF token
            )
            .authorizeHttpRequests((requests) -> requests
                    .requestMatchers( "/login", "/index.html").permitAll() //
                    // Allow access to these endpoints
                    .anyRequest().authenticated() // Require authentication for other requests
            )
            .formLogin(form -> form // Allow everyone to see the login page
                    .defaultSuccessUrl("/users", true) // Redirect to /home
                    // after successful login
                    .failureUrl("/login?error=true") // Redirect to login page with error message
            )
            .logout(logout -> logout
                    .permitAll() // Allow everyone to access the logout functionality
                    .logoutSuccessUrl("/login?logout=true") // Redirect to login page after logout
            );

    return http.build();
}

    // Configure AuthenticationManager using AuthenticationConfiguration
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // Define PasswordEncoder bean for encoding passwords
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }

    @Bean
    public CustomAuthenticationFailureHandler customAuthenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }
}
