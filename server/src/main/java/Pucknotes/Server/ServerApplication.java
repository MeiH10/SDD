package Pucknotes.Server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

/**
 * This is the main entry point for the Spring Boot application.
 * It is annotated with @SpringBootApplication, which indicates
 * that it serves as the primary configuration class for the application.
 */
@SpringBootApplication
@EnableWebMvc
public class ServerApplication {

    /**
     * Creates a bean of type CommonsRequestLoggingFilter.
     * This filter is used to log incoming HTTP requests. The logging
     * includes client information, query strings, and payloads
     * with a maximum payload length set to 64000 bytes. 
     * 
     * @return An instance of CommonsRequestLoggingFilter configured with logging options.
     */
    @Bean
    public CommonsRequestLoggingFilter requestLoggingFilter() {
        CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter();
        loggingFilter.setIncludeClientInfo(true); // This includes client information in the logs.
        loggingFilter.setIncludeQueryString(true); // This includes the query string in the logs.
        loggingFilter.setIncludePayload(true); // This includes the payload in the logs.
        loggingFilter.setMaxPayloadLength(64000); // This sets the maximum payload length to log.
        return loggingFilter; // Return the configured logging filter.
    }

    @Bean
    RouterFunction<ServerResponse> spaRouter() {
        var path = RequestPredicates.path("/assets/**")
            .or(RequestPredicates.path("/api/**"))
            .or(RequestPredicates.path("/favicon.ico"))
            .negate();

        return RouterFunctions.route()
            .resource(path, new ClassPathResource("public/index.html"))
            .build();
    }

    /**
     * Configures Cross-Origin Resource Sharing (CORS) settings
     * for the application. This method returns a WebMvcConfigurer
     * to define CORS mapping for endpoints that match /api/**.
     * 
     * The allowed origin is set to "http://localhost:5173", allowing
     * requests from this origin to use GET, POST, PUT, DELETE, and OPTIONS methods.
     * All headers are permitted and credentials can be included in requests.
     * 
     * @return An instance of WebMvcConfigurer that configures CORS.
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**") // This applies the CORS configuration to all /api/** endpoints.
                    .allowedOrigins("http://localhost:8080") // Only allow requests from this origin.
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Specify allowed HTTP methods.
                    .allowedHeaders("*") // Allow all headers in requests.
                    .allowCredentials(true); // Allows credentials to be included in requests.
                
                registry.addMapping("/**") // This applies the CORS configuration to all /api/** endpoints.
                    .allowedOrigins("http://localhost:8080") // Only allow requests from this origin.
                    .allowedMethods("GET") // Specify allowed HTTP methods.
                    .allowedHeaders("*") // Allow all headers in requests.
                    .allowCredentials(true); // Allows credentials to be included in requests.
            }
        };
    }

    /**
     * The main method that serves as the entry point to the Java application.
     * It delegates to Spring Boot's SpringApplication.run method to launch the application.
     * 
     * @param args Command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args); // Launch the Spring Boot application.
    }
}
