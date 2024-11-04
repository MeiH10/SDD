package Pucknotes.Server.session;

// import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // @Bean
    // public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    //     http
    //             .csrf().disable()  // Disable CSRF for APIs (or configure it properly)
    //             .authorizeRequests()
    //             .antMatchers("/login", "/register").permitAll()
    //             .anyRequest().authenticated()
    //             .and()
    //             .sessionManagement()
    //             .sessionFixation().newSession()  // Prevent session fixation attacks
    //             .maximumSessions(1);  // Limits the user to one active session
    //     return http.build();
    // }
}
