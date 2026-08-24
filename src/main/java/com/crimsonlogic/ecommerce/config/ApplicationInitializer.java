package com.crimsonlogic.ecommerce.config;

import javax.servlet.Filter;

import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

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
    
 // ==========================================================
    // ENABLE 404 EXCEPTION HANDLING
    // ==========================================================

    @Override
    protected DispatcherServlet createDispatcherServlet(
            org.springframework.web.context.WebApplicationContext servletAppContext) {

        DispatcherServlet dispatcherServlet =
                new DispatcherServlet(servletAppContext);

        dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);

        return dispatcherServlet;
    }
}