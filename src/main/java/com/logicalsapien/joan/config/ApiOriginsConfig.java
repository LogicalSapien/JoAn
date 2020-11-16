package com.logicalsapien.joan.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class to manage CORS.
 */
@Configuration
public class ApiOriginsConfig implements WebMvcConfigurer {

    /**
     * Origins allowed to accept request.
     */
    @Value("${origins.allowed}")
    private String[] originsAllowed;

    /**
     * Override cors mappings
     * @param registry Cors Registry
     */
    public void addCorsMappings(final CorsRegistry registry) {
        String[] exposeHeaders = {"Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"};

        registry.addMapping("/**").allowedOrigins(originsAllowed)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .exposedHeaders(exposeHeaders).allowCredentials(true);
    }

}
