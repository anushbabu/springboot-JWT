package com.spring.security.spring_jwt_security;

import com.spring.security.spring_jwt_security.util.JWTUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.validator.constraints.CodePointLength;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtFilter extends OncePerRequestFilter {


    @Autowired
    private JWTUtil JwtUtil;

    // Override the shouldNotFilter method to skip filtering for public endpoints
    /*
      This method checks the request URI and returns true for public endpoints ("/newuser" and "/login"),
      indicating that the filter should not be applied to those requests.
      For all other requests, it returns false, allowing the filter to process them.
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        // Skip filtering for public endpoints
        return path.equals("/newuser") || path.equals("/login");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authheader = request.getHeader("Authorization");
        String token = null;

        if (authheader == null || !authheader.startsWith("Bearer")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header");
            return;
        }
            token = authheader.substring(7);

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            Claims claims = JwtUtil.extractAllClaims(token);
            if (!JwtUtil.extractExpiration(token).before(new java.util.Date())) {
                String username = claims.getSubject();
                if (username != null && !username.isEmpty()) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            username, null, "ADMIN".equals(claims.get("role")) ? Collections.singletonList(() -> "ADMIN") : Collections.singletonList(() -> "USER")
                    );
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    // Set the authentication in the security context
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

                } else {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token: missing username");
                    return;
                }
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token has expired");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
