/*
 * ─────────────────────────────────────────────────────────────
 * 📁 FILE: src/main/java/com/scrs/config/CorsConfig.java
 * 🏗  LAYER: Backend — Configuration Layer
 * 📋 ROLE: Global Cross-Origin Resource Sharing (CORS) configuration.
 *          Allows the Frontend (running on a local file scheme or a different port)
 *          to make REST API requests to the Spring Boot backend (localhost:8080).
 * ─────────────────────────────────────────────────────────────
 */

package com.scrs.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
    }
}
