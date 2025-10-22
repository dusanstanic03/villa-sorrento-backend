/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dusan.villa_sorrento_backend.config;

/**
 *
 * @author Dusan
 */


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // primena CORS-a na sve endpointe
                        .allowedOrigins("http://localhost:3000") // dozvola za zahteve iz React aplikacije koja je na portu 3000
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // dozvoljene http metode
                        .allowedHeaders("*") // dozvola za sve headere
                        .allowCredentials(true); // dozvola za slanje cookies/auth header-a
            }
        };
    }
}
