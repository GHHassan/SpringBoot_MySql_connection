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

@Configuration
@EnableWebSecurity
public class WebSecurity {

    // Define SecurityFilterChain to configure HTTP security
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http)
//            throws Exception {
//        http.authorizeHttpRequests(
//                (requests) -> requests.requestMatchers("/", "/users")
//                        .permitAll()
//                        .anyRequest().authenticated()).formLogin(
//                (form) -> form.loginPage("/users").successHandler(
//                                customAuthenticationSuccessHandler())  // Use custom success handler
//                        .failureHandler(
//                                customAuthenticationFailureHandler())  // Use custom failure handler
//                        .permitAll()).logout(LogoutConfigurer::permitAll);
//
//        return http.build();
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)  // Disable CSRF protection
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/", "/users").permitAll()  // Allow access to these endpoints
                        .anyRequest().authenticated()  // Require authentication for other requests
                )
                .formLogin(AbstractHttpConfigurer::disable)  // Disable form-based login
                .httpBasic(AbstractHttpConfigurer::disable)  // Disable HTTP Basic authentication
                .logout(LogoutConfigurer::permitAll);  // Allow everyone to access the logout functionality

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
