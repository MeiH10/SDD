package Pucknotes.Server.Session;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

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
                .csrf(c -> c.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(r -> r
                        .requestMatchers("/api/**").permitAll()
                        .requestMatchers("/**").permitAll()
                        .requestMatchers("/v2/api-docs", "/configuration/**", "/swagger*/**", "/webjars/**").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(s -> s
                        .sessionFixation()
                        .newSession()
                        .maximumSessions(1))
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
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(
            "http://localhost:5173",
            "http://localhost:8080",
            "https://pucknotes.up.railway.app"
        ));
        configuration.setAllowedMethods(List.of(
            "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD"
        ));
        configuration.setAllowedHeaders(List.of(
            "Authorization",
            "Cache-Control",
            "Content-Type",
            "Accept",
            "Origin",
            "X-Requested-With",
            "Access-Control-Request-Method",
            "Access-Control-Request-Headers"
        ));
        configuration.setExposedHeaders(List.of(
            "Access-Control-Allow-Origin",
            "Access-Control-Allow-Credentials"
        ));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(Duration.ofHours(24));
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}