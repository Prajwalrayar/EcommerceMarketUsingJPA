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
        "com.crimsonlogic.ecommerce.handler",
        "com.crimsonlogic.ecommerce.interceptor"
})
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private AuthenticationInterceptor authenticationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor)
                // Secure sensitive endpoints:
                .addPathPatterns(
                        "/api/customer/**",
                        "/api/admin/**",
                        "/api/inventory/**",
                        "/api/orders/**",
                        "/api/addresses/**",
                        "/api/wishlist/**",
                        "/api/reviews/add",
                        "/api/reviews/seller/**",
                        "/api/categories/add",
                        "/api/products" // Secures POST/PUT (Adding/Editing products)
                )

                // Public endpoints open to everyone (Browsing, Searching, Reviews):
                .excludePathPatterns(
                        "/api/auth/**",
                        "/api/categories/all",
                        "/api/products/paged",
                        "/api/products/filter",
                        "/api/products/search",
                        "/api/products/category/**",
                        "/api/reviews/product/**"
                );
    }
}