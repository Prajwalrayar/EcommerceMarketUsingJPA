package com.crimsonlogic.ecommerce.config;

import com.crimsonlogic.ecommerce.interceptor.AuthenticationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {
        "com.crimsonlogic.ecommerce.component",
        "com.crimsonlogic.ecommerce.controller",
        "com.crimsonlogic.ecommerce.handler",     // Ensures your GlobalExceptionHandler works
        "com.crimsonlogic.ecommerce.interceptor"  // Ensures Spring finds your Interceptor
})
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private AuthenticationInterceptor authenticationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor)
                // Secure all these API paths:
                .addPathPatterns(
                        "/api/customer/**",
                        "/api/admin/**",
                        "/api/products/**",
                        "/api/inventory/**"
                )
                // ONLY exclude the auth paths!
                .excludePathPatterns(
                        "/api/auth/**"
                );
    }
}