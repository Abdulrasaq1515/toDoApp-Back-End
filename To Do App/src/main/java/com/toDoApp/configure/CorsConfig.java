//package com.toDoApp.configure;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class CorsConfig {
//
//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/**") // apply to all endpoints
//                        .allowedOrigins("http://127.0.0.1:5500") // your frontend
//                        .allowedMethods("GET", "POST", "PUT", "DELETE","PATCH", "OPTIONS")// the mapping method allowed
//                        .allowedHeaders("*")
//                        .allowCredentials(true); // if cookies/sessions are used
//            }
//        };
//    }
//}
