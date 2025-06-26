package com.example.Personality.Config;

import com.example.Personality.Models.User;
import com.example.Personality.Services.TokenService;
import jakarta.security.auth.message.AuthException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.List;

@Component
public class Filter extends OncePerRequestFilter {
    @Autowired
    private com.example.Personality.Services.TokenService tokenService;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    private final List<String> AUTH_PERMISSION = List.of(
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/api/user/login",
            "/api/user/register"
    );

    private final List<String> PUBLIC_GET_APIS = List.of(
            "/api/test"
    );

    private boolean checkIsPublicAPI(String uri) {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        return AUTH_PERMISSION.stream().anyMatch(pattern -> pathMatcher.match(pattern, uri));
    }

    public boolean isPublicGetAPI(HttpServletRequest request) {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        String uri = request.getRequestURI();
        return "GET".equalsIgnoreCase(request.getMethod()) &&
                PUBLIC_GET_APIS.stream().anyMatch(pattern -> pathMatcher.match(pattern, uri));
    }

    @Autowired
    public Filter(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        boolean isPublicAPI = checkIsPublicAPI(request.getRequestURI());
        boolean isPublicGetAPI = isPublicGetAPI(request);
        if (isPublicAPI || isPublicGetAPI) {
            // Allow access to public APIs
            filterChain.doFilter(request, response);
        } else {
            // Check token for private APIs
            String token = getToken(request);
            if (token == null) {
                // No token provided
                resolver.resolveException(request, response, null, new AuthException("Empty token"));
                return;
            }

            // Validate token and get account details
            User account = tokenService.getAccountByToken(token);

            // Token is valid, set authentication in SecurityContext
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    account, token, account.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            // Allow access
            filterChain.doFilter(request, response);
        }
    }

    private String getToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.substring(7);
    }

}
