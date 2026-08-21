package com.crimsonlogic.ecommerce.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthenticationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null || session.getAttribute("role") == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized access. Please login.");
            return false;
        }

        String role = (String) session.getAttribute("role");

        // Role-based authorization routing
        if (uri.startsWith(contextPath + "/api/admin") && !"ADMIN".equals(role)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Admin role required.");
            return false;
        }
        if (uri.startsWith(contextPath + "/api/seller") && !"SELLER".equals(role)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Seller role required.");
            return false;
        }
        if (uri.startsWith(contextPath + "/api/customer") && !"CUSTOMER".equals(role)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Customer role required.");
            return false;
        }

        // Expose userId for controllers to retrieve cleanly
        request.setAttribute("userId", session.getAttribute("userId"));
        return true;
    }
}