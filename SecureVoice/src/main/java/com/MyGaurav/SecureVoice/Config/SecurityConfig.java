package com.MyGaurav.SecureVoice.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

// @Configuration = Spring reads this class when starting up and uses its settings
@Configuration
public class SecurityConfig {

    // This method defines ALL the security rules for our app
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(auth -> auth
                        // Public
                        .requestMatchers("/", "/index.html", "/css/**", "/js/**").permitAll()
                        .requestMatchers("/api/complaint", "/api/complaint/**").permitAll()

                        // 🔑 VERY IMPORTANT: login must come before /admin/**
                        .requestMatchers("/admin/login", "/admin/login**").permitAll()

                        // Protected admin routes
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        .anyRequest().permitAll()
                )

                // Use Spring's built-in login form
                .formLogin(form -> form
                        .loginPage("/admin/login")               // our custom login page URL
                        .loginProcessingUrl("/admin/login")      // where the form POSTs to
                        .defaultSuccessUrl("/admin/dashboard.html", true)  // go here after login
                        .failureUrl("/admin/login?error=true")   // go here if wrong password
                        .permitAll()
                )

                // Logout setup
                .logout(logout -> logout
                        .logoutUrl("/admin/logout")
                        .logoutSuccessUrl("/admin/login?logout=true")
                        .permitAll()
                )

                // Disable CSRF for our API routes (CSRF protection is for forms, not JSON APIs)
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/**", "/admin/update", "/admin/complaints", "/admin/stats")
                );

        return http.build();
    }

    // This creates the admin user in memory (no user table needed in DB)
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("Admin@123"))  // BCrypt hashes the password
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(admin);
    }

    // BCrypt is the standard way to hash passwords
    // Viva: "We never store the raw password, we store a BCrypt hash of it"
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}