package com.crimsonlogic.ecommerce.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

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

            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Unauthorized access. Please login."
            );

            return false;
        }

        /*
         * Get authenticated user information from session.
         */
        Object userId = session.getAttribute("userId");
        String role = String.valueOf(session.getAttribute("role"));

        /*
         * ADMIN authorization
         *
         * /api/admin/** can only be accessed by ADMIN.
         */
        if (uri.startsWith(contextPath + "/api/v1/admin")
                && !"ADMIN".equalsIgnoreCase(role)) {

            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Admin role required."
            );

            return false;
        }

        /*
         * SELLER authorization
         *
         * /api/seller/** can only be accessed by SELLER.
         */
        if (uri.startsWith(contextPath + "/api/v1/seller")
                && !"SELLER".equalsIgnoreCase(role)) {

            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Seller role required."
            );

            return false;
        }

        /*
         * CUSTOMER authorization
         *
         * /api/customer/** can only be accessed by CUSTOMER.
         */
        if (uri.startsWith(contextPath + "/api/v1/customer")
                && !"CUSTOMER".equalsIgnoreCase(role)) {

            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Customer role required."
            );

            return false;
        }

        /*
         * Expose authenticated user information
         * to controllers through request attributes.
         *
         * This allows controllers to use:
         *
         * @RequestAttribute("userId")
         *
         * @RequestAttribute("role")
         */
        request.setAttribute("userId", userId);
        request.setAttribute("role", role);

        /*
         * Authentication and authorization successful.
         * Continue to the controller.
         */
        return true;
    }
}