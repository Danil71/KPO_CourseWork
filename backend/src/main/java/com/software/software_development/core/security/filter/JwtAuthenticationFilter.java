package com.software.software_development.core.security.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.software.software_development.core.configuration.Constants;
import com.software.software_development.core.error.ErrorDetails;
import com.software.software_development.core.security.CustomUserDetailService;
import com.software.software_development.core.utility.JwtUtils;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;
    private final CustomUserDetailService customUserDetailService;

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        if (path.equals(Constants.API_URL + Constants.AUTH_URL + "/refresh-token") ||
                path.equals(Constants.API_URL + Constants.AUTH_URL + "/refresh-token-direct") ||
                path.equals(Constants.API_URL + Constants.AUTH_URL + Constants.LOGIN_URL) ||
                path.equals(Constants.API_URL + Constants.AUTH_URL + "/verify-otp") ||
                path.equals(Constants.API_URL + Constants.AUTH_URL + "/verify-otp-direct") ||
                path.equals(Constants.API_URL + Constants.AUTH_URL + "/logout") ||
                path.equals(Constants.API_URL + Constants.AUTH_URL + "/logout-direct")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final var authorizationHeader = request.getHeader("Authorization");

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                final var token = authorizationHeader.substring(7);
                final var userId = jwtUtils.extractUserId(token);

                if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = customUserDetailService.loadUserByUsername(String.valueOf(userId));

                    if (jwtUtils.validateToken(token, userDetails)) {
                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            }

            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException ex) {
            sendErrorResponse(response, "EXPIRED_JWT", ex.getMessage(), request.getRequestURI(), HttpServletResponse.SC_UNAUTHORIZED);
        } catch (JwtException ex) {
            sendErrorResponse(response, "INVALID_JWT", ex.getMessage(), request.getRequestURI(), HttpServletResponse.SC_UNAUTHORIZED);
        } catch (Exception ex) {
            sendErrorResponse(response, "AUTH_ERROR", "Authentication error", request.getRequestURI(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void sendErrorResponse(HttpServletResponse response, String errorCode, String message, String path, int statusCode)
            throws IOException {
        ErrorDetails errorDetails = new ErrorDetails(errorCode, message, path);

        response.setStatus(statusCode);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        String json = mapper.writeValueAsString(errorDetails);
        response.getWriter().write(json);
    }
}

