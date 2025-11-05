package com.example.demo20.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Bộ lọc kiểm tra JWT trong mỗi request.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();

        // 🔸 Bỏ qua xác thực cho các API public
        if (isPublicEndpoint(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 🔸 Lấy Authorization header
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                    "Missing or invalid Authorization header");
            return;
        }

        String token = authHeader.substring(7);

        try {
            String username = jwtService.extractUsername(token);

            if (!jwtService.validateToken(token, username)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                        "Invalid or expired JWT token");
                return;
            }

            // ✅ Token hợp lệ, cho phép request đi tiếp
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                    "Invalid JWT token: " + e.getMessage());
        }
    }

    // Hàm kiểm tra endpoint công khai
    private boolean isPublicEndpoint(String path) {
        return path.startsWith("/api/auth")
                || path.startsWith("/api/menu")
                || path.startsWith("/api/tables")
                || path.startsWith("/api/orders");
    }
}
