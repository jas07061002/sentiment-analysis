package com.example.sentimentanalyzer.config;

import com.example.sentimentanalyzer.service.KeycloakTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@Slf4j
public class TokenFilter extends OncePerRequestFilter {

    @Value("${keycloak.issuer-uri}")
    private String issuerUri;
    private final KeycloakTokenService tokenService;

    public TokenFilter(KeycloakTokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if ("/analyze-sentiment".equals(request.getRequestURI())) {
            try {
                String token = tokenService.getAccessToken();
                log.debug("Setting security context with token: {}", token);

                // Create a Jwt object (minimal claims for simplicity)
                Jwt jwt = Jwt.withTokenValue(token)
                        .header("alg", "RS256")
                        .issuer(issuerUri)
                        .claim("scope", "profile email")
                        .build();

                JwtAuthenticationToken authentication = new JwtAuthenticationToken(jwt, Collections.emptyList());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                log.error("Failed to set security context", e);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token fetch failed");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}