package com.example.currency.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration class for setting up security in the application.
 * This class configures user authentication, password encoding, and security rules.
 */
@Configuration
@EnableMethodSecurity // Enables method-level security annotations like @PreAuthorize.
public class SecurityConfig {

    /**
     * Configures an in-memory user details service with predefined users and roles.
     *
     * @return an instance of {@link InMemoryUserDetailsManager}.
     */
    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        return new InMemoryUserDetailsManager(
                // Predefined user with the role "EMPLOYEE".
                User.withUsername("employee")
                        .password(passwordEncoder().encode("password"))
                        .roles("EMPLOYEE")
                        .build(),
                // Predefined user with the role "AFFILIATE".
                User.withUsername("affiliate")
                        .password(passwordEncoder().encode("password"))
                        .roles("AFFILIATE")
                        .build(),
                // Predefined user with the role "ADMIN".
                User.withUsername("admin")
                        .password(passwordEncoder().encode("admin"))
                        .roles("ADMIN")
                        .build()
        );
    }

    /**
     * Configures the password encoder used for encoding user passwords.
     * Uses BCrypt for hashing passwords securely.
     *
     * @return an instance of {@link PasswordEncoder}.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures the security filter chain for HTTP requests.
     * - Disables CSRF protection (useful in stateless APIs).
     * - Secures endpoints under "/api/**" to require authentication.
     * - Allows all other requests without authentication.
     * - Enables HTTP Basic authentication for simplicity.
     *
     * @param http the {@link HttpSecurity} object to configure.
     * @return the configured {@link SecurityFilterChain}.
     * @throws Exception if an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disables CSRF protection.
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/**").authenticated() // Requires authentication for /api/**.
                        .anyRequest().permitAll() // Allows all other requests without authentication.
                )
                .httpBasic(Customizer.withDefaults()); // Enables HTTP Basic authentication.
        return http.build();
    }
}
