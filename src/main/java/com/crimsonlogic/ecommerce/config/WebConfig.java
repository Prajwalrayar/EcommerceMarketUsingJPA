package com.crimsonlogic.ecommerce.config;

import com.crimsonlogic.ecommerce.interceptor.AuthenticationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

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
                        "/api/v1/customer/**",
                        "/api/v1/admin/**",
                        "/api/v1/inventory/**",
                        "/api/v1/orders/**",
                        "/api/v1/addresses/**",
                        "/api/v1/reviews/add",
                        "/api/v1/reviews/seller/**",
                        "/api/v1/categories/add",
                        "/api/v1/products" ,
                        "/api/v1/products/**"
                )

                // Public endpoints open to everyone (Browsing, Searching, Reviews):
                .excludePathPatterns(
                        "/api/v1/auth/**",
                        "/api/v1/categories/all",
                        "/api/v1/products/paged",
                        "/api/v1/products/filter",
                        "/api/v1/products/search",
                        "/api/v1/products/category/**",
                        "/api/v1/reviews/product/**"
                );
    }


    @Bean
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver resolver =
                new InternalResourceViewResolver();

        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");

        return resolver;
    }
}