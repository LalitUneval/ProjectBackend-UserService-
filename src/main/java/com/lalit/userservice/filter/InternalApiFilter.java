package com.lalit.userservice.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class InternalApiFilter extends OncePerRequestFilter {

    @Value("${internal.security.key}")
    private String internalKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String requestKey = request.getHeader("X-Internal-Secret");
        String authHeader = request.getHeader("Authorization");

        boolean hasValidSecret = internalKey != null && internalKey.equals(requestKey);
        boolean hasValidBearer = authHeader != null && authHeader.startsWith("Bearer ");

        // BOTH must be present and valid
        if (hasValidSecret && hasValidBearer) {
            filterChain.doFilter(request, response);
            return;
        }

        // Reject everything else
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write(
                "{\"error\": \"Unauthorized\", \"message\": \"Direct access is restricted.\"}"
        );
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/actuator") || path.startsWith("/favicon.ico");
    }
}