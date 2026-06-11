package com.example.jkr.elesafe.config;

import com.example.jkr.elesafe.service.JwtService;
import com.example.jkr.elesafe.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;

    // ── Paths that skip JWT validation completely ──────────────────────────
    private static final List<String> WHITE_LIST_PATHS = Arrays.asList(
            "/api/health",
            "/api/auth/register",
            "/api/auth/login",
            "/api/auth/refresh-token",
            "/v3/api-docs",
            "/v3/api-docs.yaml",
            "/swagger-ui.html",
            "/swagger-ui",
            "/swagger-resources",
            "/webjars",
            "/configuration/ui",
            "/configuration/security",
            "/favicon.ico",
            "/error",
            "/ws"
    );

    private boolean isWhitelisted(String path) {
        return WHITE_LIST_PATHS.stream().anyMatch(path::startsWith);
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String path = request.getServletPath();

        // ── Skip JWT check for whitelisted paths ──────────────────────────
        if (isWhitelisted(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        // ── Extract Authorization header ───────────────────────────────────
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // No token present — let Spring Security handle 401
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // ── Parse JWT token ────────────────────────────────────────────────
        jwt = authHeader.substring(7);

        try {
            username = jwtService.extractUsername(jwt);
        } catch (Exception e) {
            // Malformed or invalid token — reject cleanly
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Invalid or malformed JWT token\"}");
            response.setContentType("application/json");
            return;
        }

        // ── Authenticate if not already authenticated ──────────────────────
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
