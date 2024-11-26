package Pucknotes.Server.Session;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * This class is responsible for configuring security settings for the application.
 * It uses Spring Security to define a security filter chain and a password encoder.
 * The security configuration includes session management and access control 
 * for different API endpoints.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configures the security filter chain for the application.
     * This method sets up basic security features including:
     * 
     * - Disabling CSRF protection to simplify testing and development. 
     *   NOTE: Consider enabling CSRF protection in production environments.
     * - Allowing all requests to specific API endpoints, including Swagger 
     *   documentation and webjars for front-end resources.
     * - Requiring all other requests to be authenticated.
     * - Managing user sessions to enforce a maximum of one session per user.
     * 
     * @param http The HttpSecurity object to be configured.
     * @return The configured SecurityFilterChain object.
     * @throws Exception If any error occurs during the configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(c -> c.disable()) // Disabling CSRF can expose the application to attacks. Enable in production.
                .authorizeHttpRequests(r -> r
                        .requestMatchers("/api/**").permitAll() // Allow public access to all API endpoints.
                        .requestMatchers("/v2/api-docs", "/configuration/**", "/swagger*/**", "/webjars/**").permitAll() // Swagger resources are publicly accessible.
                        .anyRequest().authenticated()) // Require authentication for any other requests.
                .sessionManagement(s -> s
                        .sessionFixation() // Configure session fixation protection.
                        .newSession() // Create a new session for authenticated users.
                        .maximumSessions(1)) // Limit users to one active session.
                .build();
    }

    /**
     * Provides a password encoder bean that uses BCrypt hashing.
     * This encoder applies a secure hashing algorithm, which is suitable for 
     * storing user passwords securely in the database.
     * 
     * @return An instance of PasswordEncoder implemented with BCryptPasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Using BCrypt for secure password hashing.
    }
}
