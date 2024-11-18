package com.example.sqlconnect.security;

import com.example.sqlconnect.utils.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired CorsConfig corsConfig;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;


    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    /*   Security
         This method is used to define the security rules for http request to
         this API.

        corsConfigurationSource defines the cors setting
        this is at CorsConfig class
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(
                        corsConfig.corsConfigurationSource())) // Enable CORS
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/login", "/signup")
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                )
                // allows login and sign up  endpoint
                // and restricts other endpoints
                // allows users endpoint only if the user has a role
                // ROLES_USER assigned to them
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/login", "/signup").permitAll()
                        .requestMatchers("/users").hasAuthority("ROLES_USER") // this line can
                        // be totally removed if the ROLE_USER is the lowest
                        // on the hierarchy
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    /*
        This bean sets out the heirarchy of the roles that are specified on the
        database for each user
        ie. if an endpoint is given access to ROLE_USER ROLE_ADMIN will be
        automatically gain access to that.
     */
    @Bean
    public RoleHierarchy roleHierarchy() {
        String hierarchy = "ROLE_ADMIN > ROLE_USER";
        return RoleHierarchyImpl.fromHierarchy(hierarchy);
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
