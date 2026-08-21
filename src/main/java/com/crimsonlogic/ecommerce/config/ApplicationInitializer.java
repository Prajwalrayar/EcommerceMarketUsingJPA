package com.crimsonlogic.ecommerce.config;

import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;

public class ApplicationInitializer
        extends AbstractAnnotationConfigDispatcherServletInitializer {


    // ==========================================================
    // ROOT CONFIGURATION
    // ==========================================================

    @Override
    protected Class<?>[] getRootConfigClasses() {

        return new Class<?>[]{
                AppConfig.class
        };
    }


    // ==========================================================
    // WEB CONFIGURATION
    // ==========================================================

    @Override
    protected Class<?>[] getServletConfigClasses() {

        return new Class<?>[]{
                WebConfig.class
        };
    }


    // ==========================================================
    // DISPATCHER SERVLET MAPPING
    // ==========================================================

    @Override
    protected String[] getServletMappings() {

        return new String[]{
                "/"
        };
    }


    // ==========================================================
    // FILTERS
    // ==========================================================

    @Override
    protected Filter[] getServletFilters() {

        CharacterEncodingFilter encodingFilter =
                new CharacterEncodingFilter();

        encodingFilter.setEncoding("UTF-8");
        encodingFilter.setForceEncoding(true);

        return new Filter[]{
                encodingFilter
        };
    }
}