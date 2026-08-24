package com.crimsonlogic.ecommerce.interceptor;

import com.crimsonlogic.ecommerce.handler.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    /*
     * ObjectMapper is created directly.
     * No Spring bean/dependency is required.
     */
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {

        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();

        /*
         * Get the existing HTTP session.
         *
         * false means:
         * - If a session exists -> return it
         * - If no session exists -> return null
         * - Do NOT create a new session
         */
        HttpSession session = request.getSession(false);

        /*
         * User must be logged in.
         *
         * A valid authenticated session must contain:
         * 1. userId
         * 2. role
         */
        if (session == null
                || session.getAttribute("userId") == null
                || session.getAttribute("role") == null) {

            return writeErrorResponse(
                    response,
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Please log in to continue."
            );
        }

        /*
         * Get authenticated user information from session.
         */
        Object userId = session.getAttribute("userId");
        String role = String.valueOf(session.getAttribute("role"));

        /*
         * ADMIN authorization
         */
        if (uri.startsWith(contextPath + "/api/v1/admin")
                && !"ADMIN".equalsIgnoreCase(role)) {

            return writeErrorResponse(
                    response,
                    HttpServletResponse.SC_FORBIDDEN,
                    "You don't have permission to access this page."
            );
        }

        /*
         * SELLER authorization
         */
        if (uri.startsWith(contextPath + "/api/v1/seller")
                && !"SELLER".equalsIgnoreCase(role)) {

            return writeErrorResponse(
                    response,
                    HttpServletResponse.SC_FORBIDDEN,
                    "You don't have permission to access this page."
            );
        }

        /*
         * CUSTOMER authorization
         */
        if (uri.startsWith(contextPath + "/api/v1/customer")
                && !"CUSTOMER".equalsIgnoreCase(role)) {

            return writeErrorResponse(
                    response,
                    HttpServletResponse.SC_FORBIDDEN,
                    "You don't have permission to access this page."
            );
        }

        /*
         * Expose authenticated user information
         * to controllers through request attributes.
         */
        request.setAttribute("userId", userId);
        request.setAttribute("role", role);

        /*
         * Authentication and authorization successful.
         */
        return true;
    }

    /*
     * ==========================================================
     * USER-FRIENDLY JSON ERROR RESPONSE
     * ==========================================================
     */
    private boolean writeErrorResponse(
            HttpServletResponse response,
            int status,
            String message) throws Exception {

        ApiResponse<Object> errorResponse =
                ApiResponse.error(message);

        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        response.getWriter().write(
                objectMapper.writeValueAsString(errorResponse)
        );

        response.getWriter().flush();

        return false;
    }
}